# UBD
> UBD is a small online bookshop that sells sets of ELT coursebooks.

## Table of Contents
* [General Information](#general-information)
* [Technologies used](#technologies-used)
* [Features](#features)
* [Room for Improvement](#room-for-improvement)
* [Contact](#contact)

## General Information
- There are four entities describing the catalogue - **Publisher**, **Categoty**, **Title**, **Bookset**.
- Each **Bookset** is a level-specific part of a **Title** having a **Publisher** and a **Category**.
- Each **Bookset** can be sold at two different prices depending on the the number of sets.

## Technologies used
- RESTful API service using Spring Boot;
- local PostgreSQL database;


## Features
- full database service for each catalogue entity i.e. *save, insert, delete, update, select*;
- full user servive i.e. *save, insert, delete, update, select*;
- full JWT authorization;

## Room for Improvement

To do:
- **Order** service i.e. *save, insert, delete, update, select*.
- **User** Two-Factor Authentication.
- **User** orders history.


## Contact
Created by [@kkk12345678](https://www.linkedin.com/in/kostiantyn-korolkov-185331b7/) - feel free to contact me!


<!-- Optional -->
<!-- ## License -->
<!-- This project is open source and available under the [... License](). -->

<!-- You don't have to include all sections - just the one's relevant to your project -->
