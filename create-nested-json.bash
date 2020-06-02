#!/usr/bin/env bash
aws --endpoint-url=http://localhost:4575 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_events \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"INVALID_JSON"}}' \
    --message '{"offenderIdDisplay":"G0373GG","offenders":[{"offenderId":1025558,"bookings":[{"offenderBookId":12678}]}]}'
