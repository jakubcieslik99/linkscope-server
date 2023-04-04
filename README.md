# <img src="https://i.ibb.co/kHCcrV0/linkscope-1.png" width="440">

## LinkScope Server 🔗

📌 A REST API server used for handling users pages containing their social media links. Project contains Java Spring Boot app
as a backend (server). Database used in this project is MySQL.

![GitHub pom.xml version (subfolder of monorepo)](https://img.shields.io/badge/server%20version-1.2.0-orange)
![GitHub top language](https://img.shields.io/github/languages/top/jakubcieslik99/linkscope-server)
![GitHub Java version](https://img.shields.io/badge/java%20version-17-yellowgreen)
![GitHub repo size](https://img.shields.io/github/repo-size/jakubcieslik99/linkscope-server)

## Features

- Managing your own page with social media links
- Adding, editing or deleting previously added social media links
- Editing your profile custom link (alias), title & bio
- Listing other users profiles with their social media links

## Endpoints Documentation

📚 Documentation of all available endpoints can be found here:
[API Documentation](https://documenter.getpostman.com/view/20607862/2s93JqRQ5s)

## Run Locally

- Clone repository

```bash
  git clone https://github.com/jakubcieslik99/linkscope-server.git
```

ℹ️ Instructions for running server app locally:

- install Maven dependencies
- configure `env.properties` file for development/testing mode
- run `App.java` file

## Deployment

ℹ️ Instructions for building and running server app in production

- Compile and build `.jar` file using Maven script `package`
- Run `.jar` file on your server

```bash
  java -jar linkscope-server.jar
```

## Environment Variables

⚙️ To run server app, you will need to add the following environment variables to your env.properties file

- `ENV`

- `PORT`

- `API_URL`

- `WEBAPP_URL`

- `MYSQL_URI`

- `MYSQL_USER`

- `MYSQL_PASSWORD`

- `JWT_ACCESS_TOKEN_SECRET`

- `JWT_REFRESH_TOKEN_SECRET`

## Languages

🔤 Available API messages languages: **EN**

## Feedback

If you have any feedback, please reach out to me at ✉️ contact@jakubcieslik.com

## Authors

- [@jakubcieslik99](https://www.github.com/jakubcieslik99)
