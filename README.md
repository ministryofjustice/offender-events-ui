# offender-events-ui
Dev tool to surface recent offender events

## Running Locally

To start up a local instance of localstack to mock aws, run the command:

```docker-compose up localstack``` 

Then start the application from Intellij with active profile `localstack`.

You can now inject messages onto the topic using command `./create-prison-movements-messages-local.bash`.
