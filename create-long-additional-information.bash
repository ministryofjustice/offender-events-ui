#!/usr/bin/env bash
aws --endpoint-url=http://localhost:4566 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_events \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"prison-offender-events.prisoner.received"}}' \
    --message '{"version":"1.0","description":"A prisoner has been received into prison","occurredAt":"2023-10-25T15:21:47+01:00","personReference":"{identifiers=[{type=NOMS, value=A6887DZ}]}","additionalInformation":"{nomsNumber=A6887DZ, reason=ADMISSION, details=ACTIVE IN:ADM-N, currentLocation=IN_PRISON, prisonId=LII, nomisMovementReasonCode=N, currentPrisonStatus=UNDER_PRISON_CARE}"}'
