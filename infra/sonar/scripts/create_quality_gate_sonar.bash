#!/usr/bin/env bash

SONAR_URL="http://localhost:9000/api"
SONAR_AUTH="admin:dev!"

wait_sonarqube_up() {
    sonar_status="DOWN"
    printf "INFO initiating connection with SonarQube.\n"

    while [ "${sonar_status}" != "UP" ]; do
        sleep 5
        printf "INFO retrieving SonarQube's service status.\n"
        sonar_status=$(curl -s --get "${SONAR_URL}/system/status" | jq -r '.status')
        printf "INFO SonarQube is ${sonar_status}, expecting it to be UP.\n"
    done

    curl -u admin:admin --request POST "${SONAR_URL}/users/change_password?login=admin&previousPassword=admin&password=dev!"

    printf "INFO SonarQube is ${sonar_status}."
}

install_mutation_plugin() {
    curl --request POST -su ${SONAR_AUTH} \
        --data-urlencode "key=sonar.plugins.risk.consent" \
        --data-urlencode "value=ACCEPTED" \
        "${SONAR_URL}/settings/set"

    res=$(curl --request POST -su ${SONAR_AUTH} \
        --data-urlencode "key=mutationanalysis" \
        "${SONAR_URL}/plugins/install")

    if [ "$(echo "${res}" | jq '(.errors | length)')" ] >"1"; then
        printf "WARNING impossible install mutation plugin. $(echo "${res}" | jq '.errors[].msg')\n"
        return
    fi

    curl -u ${SONAR_AUTH} --request POST "${SONAR_URL}/system/restart"
    printf "INFO successfully install mutation plugin\n"
}

add_condition_to_quality_gate() {
    gate_id=$1
    metric_key=$2
    metric_operator=$3
    metric_errors=$4

    printf "INFO adding quality gate condition: ${metric_key} ${metric_operator} ${metric_errors}.\n"

    threshold=()
    if [ "${metric_errors}" != "none" ]; then
        threshold=("--data-urlencode" "error=${metric_errors}")
    fi

    res=$(curl --request POST -su ${SONAR_AUTH} \
        --data-urlencode "gateId=${gate_id}" \
        --data-urlencode "metric=${metric_key}" \
        --data-urlencode "op=${metric_operator}" \
        "${threshold[@]}" \
        "${SONAR_URL}/qualitygates/create_condition")
    if [ "$(echo "${res}" | jq '(.errors | length)')" == "0" ]; then
        printf "INFO metric ${metric_key} condition successfully added.\n"
    else
        printf "WARNING impossible to add ${metric_key} condition $(echo "${res}" | jq '.errors[].msg')\n"
    fi
}

create_quality_gate() {
    printf "INFO creating quality gate.\n"
    res=$(curl --request POST -su ${SONAR_AUTH} \
        --data-urlencode "name=BARE" \
        "${SONAR_URL}/qualitygates/create")
    if [ "$(echo "${res}" | jq '(.errors | length)')" == "0" ]; then
        printf "INFO successfully created quality gate... now configuring it.\n"
    else
        printf "WARNING impossible to create quality gate $(echo "${res}" | jq '.errors[].msg')\n"
        return
    fi

    # Retrieve quality gates ID
    printf "INFO retrieving quality gate ID."
    res=$(curl --get -su ${SONAR_AUTH} \
        --data-urlencode "name=BARE" \
        "${SONAR_URL}/qualitygates/show")
    if [ "$(echo "${res}" | jq '(.errors | length)')" == "0" ]; then
        GATEID=$(echo ${res} | jq '.id' | tail -c +2 | head -c -2)
        printf "INFO successfully retrieved quality gate ID (ID=$GATEID).\n"
    else
        printf "ERROR impossible to reach quality gate ID $(echo "${res}" | jq '.errors[].msg')\n"
        return
    fi

    ## Setting it as default quality gate
    printf "INFO setting quality gate as default gate.\n"
    printf ${GATEID}
    res=$(curl --request POST -su ${SONAR_AUTH} \
        --data-urlencode "id=${GATEID}" \
        "${SONAR_URL}/qualitygates/set_as_default")
    if [ -z "$res" ]; then
        printf "INFO successfully set quality gate as default gate.\n"
    else
        printf "WARNING impossible to set quality gate as default gate $(echo "${res}" | jq '.errors[].msg')\n"
        return
    fi

    ## Adding all conditions of the JSON file
    printf "INFO adding all conditions of cnes-quality-gate.json to the gate.\n"
    len=$(jq '(.conditions | length)' ./scripts/quality_gate_custom.json)
    quality_gate=$(jq '(.conditions)' ./scripts/quality_gate_custom.json)
    for i in $(seq 0 $((len - 1))); do
        metric=$(echo "$quality_gate" | jq -r '(.['"$i"'].metric)')
        op=$(echo "$quality_gate" | jq -r '(.['"$i"'].op)')
        error=$(echo "$quality_gate" | jq -r '(.['"$i"'].error)')
        add_condition_to_quality_gate "$GATEID" "$metric" "$op" "$error"
    done
}

create_quality_profile() {
    printf "INFO creating quality profile.\n"
    res=$(curl --location --request POST -u ${SONAR_AUTH} \
        --data-urlencode "language=java" \
        --data-urlencode "name=Sonar_way_and_Mutation" \
        "${SONAR_URL}/qualityprofiles/create")
    if [ "$(echo "${res}" | jq '(.errors | length)')" == "0" ]; then
        printf "INFO successfully created quality profile... now configuring it.\n"
    else
        printf "WARNING impossible to create quality profile $(echo "${res}" | jq '.errors[].msg')\n"
        return
    fi

    printf "INFO change parent of quality profile Sonar_way_and_Mutation to Sonar way.\n"
    res=$(curl --location --request POST -u ${SONAR_AUTH} -w "%{http_code}\n" \
        --data-urlencode "language=java" \
        --data-urlencode "parentQualityProfile=Sonar way" \
        --data-urlencode "qualityProfile=Sonar_way_and_Mutation" \
        "${SONAR_URL}/qualityprofiles/change_parent")
    if [ "$(echo "${res}")" == "204" ]; then
        printf "INFO successfully change parent of quality profile Sonar_way_and_Mutation to Sonar way.\n"
    else
        printf "WARNING impossible change parent of quality profile Sonar_way_and_Mutation to Sonar way $(echo "${res}" | jq '.errors[].msg')\n"
        return
    fi

    printf "INFO set Sonar_way_and_Mutation as default quality profile.\n"
    res=$(curl --location --request POST -u ${SONAR_AUTH} -w "%{http_code}\n" \
        --data-urlencode "language=java" \
        --data-urlencode "qualityProfile=Sonar_way_and_Mutation" \
        "${SONAR_URL}/qualityprofiles/set_default")
    if [ "$(echo "${res}")" == "204" ]; then
        printf "INFO successfully set Sonar_way_and_Mutation as default quality profile.\n"
    else
        printf "WARNING impossible set Sonar_way_and_Mutation as default quality profile $(echo "${res}" | jq '.errors[].msg')\n"
        return
    fi

    printf "INFO retrieving target quality profile key."
    res=$(curl --location --get -u ${SONAR_AUTH} \
        --data-urlencode "defaults=true" \
        --data-urlencode "language=java" \
        --data-urlencode "qualityProfile=Sonar_way_and_Mutation" \
        "${SONAR_URL}/qualityprofiles/search")
    printf ${res}
    if [ "$(echo "${res}" | jq '(.errors | length)')" == "0" ]; then
        TARGETKEY=$(echo ${res} | jq '.profiles[0].key' | tail -c +2 | head -c -2)
        printf "INFO successfully retrieved target quality profile key (KEY=$TARGETKEY).\n"
    else
        printf "ERROR impossible to reach target quality profile key $(echo "${res}" | jq '.errors[].msg')\n"
        return
    fi

    printf "INFO retrieving source quality profile key."
    res=$(curl --location --get -u ${SONAR_AUTH} \
        --data-urlencode "language=java" \
        --data-urlencode "qualityProfile=Mutation Analysis" \
        "${SONAR_URL}/qualityprofiles/search")
    if [ "$(echo "${res}" | jq '(.errors | length)')" == "0" ]; then
        SOURCEKEY=$(echo ${res} | jq '.profiles[0].key' | tail -c +2 | head -c -2)
        printf "INFO successfully retrieved source quality profile key (KEY=$SOURCEKEY).\n"
    else
        printf "ERROR impossible to reach source quality profile key $(echo "${res}" | jq '.errors[].msg')\n"
        return
    fi

    printf "INFO active rules of Mutation Analysis on Sonar_way_and_Mutation profile.\n"
    res=$(curl -v --location --request POST -u ${SONAR_AUTH} \
        --data-urlencode "language=java" \
        --data-urlencode "tags=mutation-operator" \
        --data-urlencode "qprofile=${SOURCEKEY}" \
        --data-urlencode "targetKey=${TARGETKEY}" \
        "${SONAR_URL}/qualityprofiles/activate_rules")
    if [ "$(echo "${res}" | jq '(.errors | length)')" == "0" ]; then
        printf "INFO successfully active rules of Mutation Analysis on Sonar_way_and_Mutation profile.\n"
    else
        printf "WARNING impossible active rules of Mutation Analysis on Sonar_way_and_Mutation profile $(echo "${res}" | jq '.errors[].msg')\n"
        return
    fi

}

wait_sonarqube_up
install_mutation_plugin
wait_sonarqube_up
create_quality_gate
create_quality_profile