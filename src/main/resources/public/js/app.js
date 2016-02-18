(function(){

    'use strict';

    function Owner($resource) {
        return $resource('/owners/:ownerId', {}, {

        });
    }

    function Company($resource) {
        return $resource('/companies/:companyId', {}, {
            query : { url : '/companies/:companyId?sort=id,desc', isArray: false },
            save: {method: 'POST', isArray: false},
            update : { method: 'PATCH' },
            addOwner : {method : 'POST', url: '/companies/:companyId/owners', headers: {'Content-Type': 'text/uri-list'}}
        });
    }

    function CompaniesController(Owner, Company, $http, $scope, $rootScope, $modal) {

        $scope.showNewCompanyForm = false;
        $scope.alert = null;
        $scope.companies = [];
        $scope.company = new Company();

        $scope.currentPage = 1;
        $scope.pageSize=10;
        $scope.totalItems=0;

        $scope.createNewCompany = function(){
            $scope.company.$save({}, function(){
                $scope.company = new Company();
                $rootScope.showAlert('New company was added', 'success', $scope);
                $scope.currentPage = 1;
                $scope.goToPage();
                $rootScope.clearAlert(2000, $scope);
            })
        }




        $scope.goToPage = function() {
            Company.query({
                 page : $scope.currentPage - 1,
                 size : $scope.pageSize
            }, function(response){
                 $scope.companies = response._embedded.companies;
                 $scope.totalItems =response.page.totalElements;
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
                            $scope.goToPage();
                        }
                    });
            });

        };

        $scope.goToPage();
    }

    angular.module('app', ['ngResource', 'ui.bootstrap']).run(function($rootScope, $timeout) {
        $rootScope.showAlert = function(text, clazz, scope){
             scope.alert = text;
             scope.alertClass = clazz;
        }

        $rootScope.clearAlert = function(delay, scope) {
             $timeout(function(){
                  scope.alert = null;
                }, delay);
             }

    });

    angular.module('app').factory('Owner', ['$resource', Owner]);

    angular.module('app').factory('Company', ['$resource', Company]);

    angular.module('app').controller('CompaniesController', ['Owner', 'Company', '$http', '$scope', '$rootScope', '$modal', CompaniesController]);

    angular.module('app').controller('CompanyDetailsCtrl', function ($scope, $modalInstance, $http, $rootScope, Company, Owner, company) {

      $scope.company = company;

      $scope.owner = null;

      var dirty = false;


      $scope.ok = function () {
        $modalInstance.close(dirty);
      };

      $scope.findOwners = function(name){
         return $http.get('/owners/search/findByNameLike', {
            params : {
               name : '%' + name + '%'
            }
          }).then(function(response) {
            if(angular.isDefined(response.data._embedded.owners)){
               return response.data._embedded.owners;
            } else {
               return [];
            }
          });
       }

       $scope.updateCompany = function(){
           Company.update({
                companyId : company.id
           }, {
                name : company.name,
                address: company.address,
                city: company.city,
                country: company.country,
                email: company.email,
                phoneNumber: company.phoneNumber
           }, function(response){
                $rootScope.showAlert('Company updated', 'success', $scope);
                dirty = true;
                $rootScope.clearAlert(3000, $scope);
           }, function(errorResponse){
                $rootScope.showAlert(errorResponse.errorCode, 'error', $scope);
           })
        }

        var addOwner = function(ownerName, ownerUri){
            Company.addOwner(
                {companyId : $scope.company.id}, ownerUri.href,
                function(response){
                    $rootScope.showAlert(ownerName + ' added as owner', 'success', $scope);
                    $scope.company.owners.push({name : ownerName});
                    $rootScope.clearAlert(5000, $scope);
                }, function(errorResponse){
                    $rootScope.showAlert(ownerName + ' not added. Error: ' + errorResponse.status , 'warning', $scope);
                })
        }

        $scope.addNewOwner = function(){
            if(angular.isObject($scope.owner)){
                addOwner($scope.owner.name, $scope.owner._links.self);
            }else{
            Owner.save({}, {
            name : $scope.owner}, function(response){
                addOwner(response.name, response._links.self);
            }, function(errorResponse){
                $rootScope.showAlert('Error: ' + errorResponse.status + ' ' + errorResponse.statusText, 'danger', $scope);
            });
            }
            $scope.owner = null;
        }
    });

})();