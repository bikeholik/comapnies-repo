(function(){

    'use strict';

    function Owner($resource) {
        return $resource('/owners/:ownerId', {}, {
            query: {url : 'owners/search/findByNameLike?name=%:name%', isArray: false}
        });
    }

    function Company($resource) {
        return $resource('/companies/:companyId', {}, {
            query : { url : '/companies/:companyId?sort=id,desc', isArray: false },
            save: {method: 'POST', isArray: false}
        });
    }

    function CompaniesController(Owner, Company, $http, $scope, $modal, $timeout) {

        $scope.showNewCompanyForm = false;
        $scope.alert = null;
        $scope.companies = [];
        $scope.company = new Company();

        var currentPage = 0;

        $scope.createNewCompany = function(){
            $scope.company.$save({}, function(){
                $scope.company = new Company();
                showAlert('Company added', 'success');
                $scope.goToPage(0);
                $scope.clearAlert(2000);
            })
        }


        $scope.findOwners = function(name){
            return $http.get('/owners/search/findByNameLike', {
                    params : {
                        name : '%' + prefix + '%'
                    }
                }).then(function(response) {
                    return response.data;
                });
        }

        var showAlert = function(text, clazz){
            $scope.alert = text;
            $scope.alertClass = clazz;
        }

        $scope.clearAlert = function(delay) {
            $timeout(function(){
                $scope.alert = null;
            }, delay);
        }


        $scope.goToPage = function(pageNumber) {
            Company.query({
                        number : pageNumber
                    }, function(response){
                        $scope.companies = response._embedded.companies;
                        currentPage = pageNumber;
                    });
        }

        $scope.getCompanyDetails = function(company){

            Company.get({
                companyId : company.id,
                projection : 'inlineOwner'
                }, function(response){
                    var modalInstance = $modal.open({
                      animation: $scope.animationsEnabled,
                      templateUrl: 'companyDetails.html',
                      controller: 'CompanyDetailsCtrl',
                      resolve: {
                        company: function () {
                          return response;
                        }
                      }
                    });

                    modalInstance.result.then(function(reload){
                        if(reload){
                            $scope.goToPage(currentPage);
                        }
                    });
            });

        };

        $scope.goToPage(currentPage);
    }

    angular.module('app', ['ngResource', 'ui.bootstrap']).run(function($rootScope) {


    });

    angular.module('app').factory('Owner', ['$resource', Owner]);

    angular.module('app').factory('Company', ['$resource', Company]);

    angular.module('app').controller('CompaniesController', ['Owner', 'Company', '$http', '$scope', '$modal', '$timeout', CompaniesController]);

    angular.module('app').controller('CompanyDetailsCtrl', function ($scope, $modalInstance, $http, Company, Owner, company) {

      $scope.company = company;


      $scope.ok = function () {
        $modalInstance.close(false);
      };


    });

})();