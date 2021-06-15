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

  - name: AWS_REGION
    value: "eu-west-2"

  - name: SQS_AWS_ACCESS_KEY_ID
    valueFrom:
      secretKeyRef:
        name: oeu-sqs-instance-output
        key: access_key_id

  - name: SQS_AWS_SECRET_ACCESS_KEY
    valueFrom:
      secretKeyRef:
        name: oeu-sqs-instance-output
        key: secret_access_key

  - name: SQS_QUEUE_NAME
    valueFrom:
      secretKeyRef:
        name: oeu-sqs-instance-output
        key: sqs_ptpu_name

  - name: SQS_AWS_DLQ_ACCESS_KEY_ID
    valueFrom:
      secretKeyRef:
        name: oeu-sqs-dl-instance-output
        key: access_key_id

  - name: SQS_AWS_DLQ_SECRET_ACCESS_KEY
    valueFrom:
      secretKeyRef:
        name: oeu-sqs-dl-instance-output
        key: secret_access_key

  - name: SQS_DLQ_NAME
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
