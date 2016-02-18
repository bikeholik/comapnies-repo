# companies-repo

companies-repo is a sample project taking advantage of [spring-data-rest](http://docs.spring.io/spring-data/rest/docs/current/reference/html/) and [angular.js](https://docs.angularjs.org/api).

## Model

Project uses simple data model consisting of two domain objects (Company and Owner) associated with many-to-many relationship. 

Starting the application built in *local* maven profile the HAL browser providing by spring is deployed which can be used to navigate in project API and domain objects structure. The mentioned browser is accessible under [http://localhost:8080/browser/index.html](http://localhost:8080/browser/index.html) when application is started locally.

## Basic API calls

Below basic resources and API calls are presented. All examples assume that service is running locally.

### Create new owner

Adding or editing owners within the company requires using the owner resource identifiers. In order to create owner following request can be sent:

`curl -i -X POST -H "Content-Type:application/json" -d '{ "name" : "Mr Smith" }' http://localhost:8080/owners`

Then from the response the *Location* header or self link from the response body can be taken to be used in the future.

### Create new company

`curl -i -X POST -H "Content-Type:application/json" -d '{ "name" : "Comp", "address": "Street X", "city": "Y", "country": "C", "email" : "comp@comp.com", "owners" : ["http://localhost:8080/owners/1" ] }' http://localhost:8080/companies`

### Get a list of all companies

`curl -i -X GET http://localhost:8080/companies`

Spring-data-rest uses automatically the Spring's HATEOS therefore all returned resources have links to themsleves and to others. Similarily when getting a list of resources paging is turned on automatically and links to next, previous, last, etc. pgaes are provided. In order to manipulate with page size the *size* parameter can be used. Particular page is identified by *page* parameter.

Actual companies list is embedded in the response. Sample response is shown below.

`
{
  "_embedded" : {
    "companies" : [ {
      "info" : "Calle Larios 1, Malaga, Spain",
      "name" : "Company-1",
      "id" : 1,
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/companies/1"
        },
        "company" : {
          "href" : "http://localhost:8080/companies/1{?projection}",
          "templated" : true
        },
        "owners" : {
          "href" : "http://localhost:8080/companies/1/owners"
        }
      }
    } ]
  },
  "_links" : {
    "first" : {
      "href" : "http://localhost:8080/companies?page=0&size=1"
    },
    "self" : {
      "href" : "http://localhost:8080/companies"
    },
    "next" : {
      "href" : "http://localhost:8080/companies?page=1&size=1"
    },
    "last" : {
      "href" : "http://localhost:8080/companies?page=26&size=1"
    },
    "profile" : {
      "href" : "http://localhost:8080/profile/companies"
    }
  },
  "page" : {
    "size" : 1,
    "totalElements" : 27,
    "totalPages" : 27,
    "number" : 0
  }
`

By default a custom projection is used so that simplied company information is returned.

### Get details about the company

Getting details of the particular company is as simple as following a *self* link provided in company listing, e.g.: `curl -i -X GET http://localhost:8080/companies/1`

### Update the company

There are to ways of updating a resource. The first is to issue a PUT request with complete body which will override the existing resource, the second is to use PATCH request to update the resource selectively. For example request like: `curl -i -X PATCH -H "Content-Type:application/json" -d '{ "city" : "Cracow" }' http://localhost:8080/companies/5` will update the city property only.

### Add owner to the company

As already mentioned to add a owner to existing company owner's identifier needs to be used. The company's associations are exposed as resources so adding a new owner can be achieved by following request: `curl -i -X POST -H "Content-Type:text/uri-list" -d 'http://localhost:8080/owners/1' http://localhost:8080/companies/4/owners`

## Error codes

 * 400 - bad request - when request validation fails
 * 404 - resource not found
 * 405 - method not allowed - for example deleting a company is not supported
 * 409 - conflict - when database constraint fails - for example creating owner with the same name twice or adding the same owner to the company twice