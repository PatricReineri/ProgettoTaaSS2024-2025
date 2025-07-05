# Progetto TAASS 2024-2025

This application was developed as part of a university project aimed at designing and implementing a system based on microservice architecture.
MagicEvents is a platform designed to manage, create, and participate in events, offering a fully personalized and interactive user experience.
The implemented microservices include the following:

## User-management Service:

This service supports two authentication mechanisms—Google OAuth and an internal authentication system. Session tokens are issued and stored in a database. An authentication filter is employed to validate the token with each request and, when necessary, automatically refresh expired tokens.