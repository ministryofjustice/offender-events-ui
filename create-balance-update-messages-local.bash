#!/usr/bin/env bash
aws --endpoint-url=http://localhost:4575 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_events \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"BALANCE_UPDATE"}}' \
    --message '{"eventId":"8638886","eventType":"BALANCE_UPDATE","eventDatetime":"2020-05-07T01:32:45.945272","rootOffenderId":2556127,"offenderIdDisplay":"G8470UP","agencyLocationId":"NMI"}'

aws --endpoint-url=http://localhost:4575 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_events \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"BALANCE_UPDATE_2"}}' \
    --message '{"eventId":"8638887","eventType":"BALANCE_UPDATE_2","eventDatetime":"2020-05-07T01:32:45.981281","rootOffenderId":2556130,"offenderIdDisplay":"G8438UP","agencyLocationId":"NMI"}'

aws --endpoint-url=http://localhost:4575 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_events \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"BALANCE_UPDATE"}}' \
    --message '{"eventId":"8638888","eventType":"BALANCE_UPDATE","eventDatetime":"2020-05-07T01:32:46.034118","rootOffenderId":2556183,"offenderIdDisplay":"G8407UP","agencyLocationId":"NMI"}'

aws --endpoint-url=http://localhost:4575 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_events \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"BALANCE_UPDATE_2"}}' \
    --message '{"eventId":"8638889","eventType":"BALANCE_UPDATE_2","eventDatetime":"2020-05-07T01:32:46.073964","rootOffenderId":2556257,"offenderIdDisplay":"G8258UP","agencyLocationId":"NMI"}'

aws --endpoint-url=http://localhost:4575 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_events \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"BALANCE_UPDATE"}}' \
    --message '{"eventId":"8638885","eventType":"BALANCE_UPDATE","eventDatetime":"2020-05-07T01:32:45.909995","rootOffenderId":2555922,"offenderIdDisplay":"G8814UP","agencyLocationId":"NMI"}'

aws --endpoint-url=http://localhost:4575 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_events \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"BALANCE_UPDATE_3"}}' \
    --message '{"eventId":"8638892","eventType":"BALANCE_UPDATE_3","eventDatetime":"2020-05-07T01:32:46.197993","rootOffenderId":2556509,"offenderIdDisplay":"G8542UP","agencyLocationId":"NMI"}'

aws --endpoint-url=http://localhost:4575 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_events \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"BALANCE_UPDATE"}}' \
    --message '{"eventId":"8638893","eventType":"BALANCE_UPDATE","eventDatetime":"2020-05-07T01:32:46.236237","rootOffenderId":2556510,"offenderIdDisplay":"G8545UP","agencyLocationId":"NMI"}'

aws --endpoint-url=http://localhost:4575 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_events \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"BALANCE_UPDATE_3"}}' \
    --message '{"eventId":"8638890","eventType":"BALANCE_UPDATE_3","eventDatetime":"2020-05-07T01:32:46.107352","rootOffenderId":2556276,"offenderIdDisplay":"G8291UP","agencyLocationId":"NMI"}'

aws --endpoint-url=http://localhost:4575 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_events \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"BALANCE_UPDATE"}}' \
    --message '{"eventId":"8638895","eventType":"BALANCE_UPDATE","eventDatetime":"2020-05-07T01:32:46.281832","rootOffenderId":2556531,"offenderIdDisplay":"G8522UP","agencyLocationId":"NMI"}'

aws --endpoint-url=http://localhost:4575 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_events \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"BALANCE_UPDATE"}}' \
    --message '{"eventId":"8638896","eventType":"BALANCE_UPDATE","eventDatetime":"2020-05-07T01:32:46.33919","rootOffenderId":2556619,"offenderIdDisplay":"G8568UP","agencyLocationId":"NMI"}'

aws --endpoint-url=http://localhost:4575 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_events \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"A_REALLY_REALLY_LONG_EVENT_TYPE"}}' \
    --message '{"eventId":"8638891","eventType":"A_REALLY_REALLY_LONG_EVENT_TYPE","eventDatetime":"2020-05-07T01:32:46.152116","rootOffenderId":2556302,"offenderIdDisplay":"G8215UP","agencyLocationId":"NMI"}'

aws --endpoint-url=http://localhost:4575 sns publish \
    --topic-arn arn:aws:sns:eu-west-2:000000000000:offender_events \
    --message-attributes '{"eventType" : { "DataType":"String", "StringValue":"BALANCE_UPDATE_2"}}' \
    --message '{"eventId":"8638898","eventType":"BALANCE_UPDATE_2","eventDatetime":"2020-05-07T01:32:46.407397","rootOffenderId":2556655,"offenderIdDisplay":"G8927UP","agencyLocationId":"NMI"}'

