# Vulenrabilities

Find vulnerable packages in project configuration files utilizig the Github Security Vulnerabilities GraphQL API.

## Usage

Send post request to http://51.20.55.124:8080/api/v1/vulnerabilities/scan

With json body 

| **Key** | **Value** |
| --- | --- |
| ecosystem | NPM |
| filecontent | Project configuration file in base64 |

### Request Body Example

```
{
    "ecosystem" : "NPM",
    "fileContent":"ewogICJuYW1lIjogIk15IEFwcGxpY2F0aW9uIiwKICAidmVyc2lvbiI6ICIxLjAuMCIsCiAgImRlcGVuZGVuY2llcyI6IHsKICAgICJkZWVwLW92ZXJyaWRlIjogIjEuMC4xIiwKICAgICJleHByZXNzIjogIjQuMTcuMSIKICB9Cn0="
}
```

filecontent - 
```
{
  "name": "My Application",
  "version": "1.0.0",
  "dependencies": {
    "deep-override": "1.0.1",
    "express": "4.20.1",
"angular": "1.5.3"
  }
}
```
> [!TIP]
> You can use the github graphql explorer
> https://docs.github.com/en/graphql/overview/explorer
