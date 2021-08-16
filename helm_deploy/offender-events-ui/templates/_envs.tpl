    {{/* vim: set filetype=mustache: */}}
{{/*
Environment variables for web and worker containers
*/}}
{{- define "deployment.envs" }}
env:
  - name: SERVER_PORT
    value: "{{ .Values.image.port }}"

  - name: JAVA_OPTS
    value: "{{ .Values.env.JAVA_OPTS }}"

  - name: SPRING_PROFILES_ACTIVE
    value: "logstash"

  - name: APPINSIGHTS_INSTRUMENTATIONKEY
    valueFrom:
      secretKeyRef:
        name: {{ template "app.name" . }}
        key: APPINSIGHTS_INSTRUMENTATIONKEY

  - name: APPLICATIONINSIGHTS_CONNECTION_STRING
    value: "InstrumentationKey=$(APPINSIGHTS_INSTRUMENTATIONKEY)"

  - name: APPLICATIONINSIGHTS_CONFIGURATION_FILE
    value: "{{ .Values.env.APPLICATIONINSIGHTS_CONFIGURATION_FILE }}"

  - name: HMPPS_SQS_QUEUES_EVENT_QUEUE_ACCESS_KEY_ID
    valueFrom:
      secretKeyRef:
        name: oeu-sqs-instance-output
        key: access_key_id

  - name: HMPPS_SQS_QUEUES_EVENT_QUEUE_SECRET_ACCESS_KEY
    valueFrom:
      secretKeyRef:
        name: oeu-sqs-instance-output
        key: secret_access_key

  - name: HMPPS_SQS_QUEUES_EVENT_QUEUE_NAME
    valueFrom:
      secretKeyRef:
        name: oeu-sqs-instance-output
        key: sqs_ptpu_name

  - name: HMPPS_SQS_QUEUES_EVENT_DLQ_ACCESS_KEY_ID
    valueFrom:
      secretKeyRef:
        name: oeu-sqs-dl-instance-output
        key: access_key_id

  - name: HMPPS_SQS_QUEUES_EVENT_DLQ_SECRET_ACCESS_KEY
    valueFrom:
      secretKeyRef:
        name: oeu-sqs-dl-instance-output
        key: secret_access_key

  - name: HMPPS_SQS_QUEUES_EVENT_DLQ_NAME
    valueFrom:
      secretKeyRef:
        name: oeu-sqs-dl-instance-output
        key: sqs_ptpu_name

  - name: SPRING_REDIS_HOST
    valueFrom:
      secretKeyRef:
        name: offender-events-ui-elasticache-redis
        key: primary_endpoint_address

  - name: SPRING_REDIS_PASSWORD
    valueFrom:
      secretKeyRef:
        name: offender-events-ui-elasticache-redis
        key: auth_token

  - name: SPRING_REDIS_SSL
    value: "true"
{{- end -}}
