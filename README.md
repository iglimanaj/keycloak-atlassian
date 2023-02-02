# keycloak-atlassian

Keycloak Social Login extension for Atlassian.


## Install

Download `keycloak-atlassian-ear-<version>.ear` from [Releases page](https://github.com/iglimanaj/keycloak-atlassian/releases).
Then deploy it into `$KEYCLOAK_HOME/standalone/deployments/` directory.

## Setup

### Atlassian

Access to [Atlassian Developer Portal](https://developer.atlassian.com/console/myapps) and create your application.
You can get Client ID and Client Secret from the created application.

#### Technical details about the endpoints

In this article [OAuth 2.0 (3LO) apps](https://developer.atlassian.com/cloud/confluence/oauth-2-3lo-apps) you can find information about the OAuth2.0 relevant endpoints that are provided by Atlassian.

### Keycloak


1. Add `atlassian` Identity Provider in the realm which you want to configure.
2. In the `atlassian` identity provider page, set `Client Id` and `Client Secret`.


## Source Build

Clone this repository and run `mvn package`.
You can see `keycloak-atlassian-ear-<version>.ear` under `ear/target` directory.


## Licence

[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)


## Author

- [Igli Manaj](https://github.com/iglimanaj) for amberSearch. Find more infos about amberSearch here https://ambersearch.de


## Credits

This project was initially based on https://github.com/wadahiro/keycloak-discord, which is maintained by [Hiroyuki Wada](https://github.com/wadahiro)