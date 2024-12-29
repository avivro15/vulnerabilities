# Vulenrabilities

Find vulnerable packages in project configuration files.

## Usage

Send post request to http://13.60.211.191:8080/api/v1/vulnerabilities/scan

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

File content - 
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

