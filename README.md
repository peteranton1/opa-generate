# opa-generate
Open Policy Agent Generate Rego from text file.

## Description
This utility generates artifacts for 
[open policy agent](https://www.openpolicyagent.org/) and
[Jira](https://www.atlassian.com/software/jira). 

Given an input file (which can be generated from your microservice)
which has syntax for each endpoint, 
"my-service-urls.txt" containing something like:

    {POST /my-service/publish/message}
        PrimaryOwner,Administrator

Then this set of classes can parse and generate the following files:

"my-service-endpoints.txt" containing:

    |POST|/my-service/publish/message|POST my-service publish message|

"my-service-opa.txt" containing:

    # POST /my-service/publish/message
    allow {
        input.method == "POST"
        input.path == ["my-service", "publish", "message"]
        token.valid
    }

The former is syntax for a row in a table in the description of a JIRA ticket, 
while the latter is the syntax in rego format for open policy agent, 
which states some rules for a request with custom token for the endpoint.

This project could be used as a basis for any other situation where an 
input file of text data with similar structure needs to 
be transformed into some output formats. 


