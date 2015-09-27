/* global angular, document, window */
'use strict';

angular.module('starter.directives', ['ngLodash'])
    .directive('gnArticleHeartBtn', function () {
        return {
            scope: {
                article: '='
            },
            template: '<button data-ng-if="article._links.heart" data-ng-click="heart(article)"' +
            ' style="z-index: 10000;"' +
            ' class="button button-fab button-assertive' +
            ' icon ion-heart waves-effect waves-button waves-light gn-article-heart-button">' +
            '</button>',
            controller: ['$scope', '$timeout', '$http', '_', function ($scope, $timeout, $http, _) {
                $scope.heart = _.debounce(function (article) {
                    $http.post(article._links.heart.href).then(function success() {
                        $scope.element.addClass('gn-article-heart-animation');
                        $timeout(function () {
                            article.resource.hearts++;
                            $scope.element.removeClass('gn-article-heart-animation');
                        }, 1000);
                    }, function error() {

                    });
                }, 1000, {leading: true, trailing: false});
            }],
            link: function (scope, element) {
                scope.element = element;
            }
        };
    })
    .directive('gnArticleStarBtn', function () {
        return {
            scope: {
                article: '='
            },
            template: '<button data-ng-click="star()"' +
            ' ng-class="{ \'ion-ios-star\': starred, \'ion-ios-star-outline\': !starred }"' +
            ' class="button button-fab button-energized' +
            ' icon waves-effect waves-button waves-light">' +
            '</button>',
            controller: ['$scope', '$timeout', '$localForage', function ($scope, $timeout, $localForage) {
                $scope.starred = false;
                var articleId = $scope.article.resource.id;
                $localForage.getItem(articleId).then(function (value) {
                    $scope.starred = value !== null;
                }, function () {
                    $scope.starred = false;
                });

                $scope.star = function () {
                    $localForage.getItem(articleId).then(function (value) {
                        $scope.starred = value === null;
                        if ($scope.starred) {
                            $localForage.setItem(articleId, $scope.article);
                        } else {
                            $localForage.removeItem(articleId);
                        }
                    }, function () {
                        $localForage.removeItem(articleId);
                        $scope.starred = false;
                    });
                };
            }],
            link: function (scope, element) {
                scope.element = element;
            }
        };
    })
    .directive('gnArticleDeleteBtn', function () {
        return {
            scope: {
                article: '='
            },
            replace: true,
            template: '<button class="button button-small button-assertive" ' +
            ' data-ng-show="article._links.remove"' +
            ' data-ng-click="remove(article)">' +
            ' <i class="icon ion-trash-b"></i> Delete' +
            '</button>',
            controller: ['$scope', '$timeout', '$state', '$stateParams', '$http',
                '$ionicLoading', '$ionicPopup', function ($scope, $timeout, $state, $stateParams, $http, $ionicLoading, $ionicPopup) {
                    $scope.remove = function (article) {
                        $ionicLoading.show({
                            template: 'Deleting...'
                        });
                        $timeout(function () {
                            $http['delete'](article._links.remove.href).then(function () {
                                $ionicLoading.hide();
                                $ionicPopup.alert({
                                    title: 'Success',
                                    template: 'Entity deleted. Reload at will.'
                                });
                            }, function () {
                                $ionicLoading.hide();
                                $ionicPopup.alert({
                                    title: 'Error',
                                    template: 'Could not delete entity.'
                                });
                            });
                        }, 500);
                    };
                }],
            link: function (scope, element) {
                scope.element = element;
            }
        };
    })
    .directive('gnArticleVerifyBtn', function () {
        return {
            scope: {
                article: '='
            },
            replace: true,
            template: '<button class="button button-small" ' +
            ' data-ng-show="article._links.verify"' +
            ' data-ng-click="verify(article)">' +
            ' <i class="icon ion-checkmark"></i> Verify' +
            '</button>',
            controller: ['$scope', '$timeout', '$state', '$stateParams', '$http',
                '$ionicLoading', '$ionicPopup', function ($scope, $timeout, $state, $stateParams, $http, $ionicLoading, $ionicPopup) {
                    $scope.verify = function (article) {
                        $ionicLoading.show({
                            template: 'Verifing...'
                        });
                        $timeout(function () {
                            $http.put(article._links.verify.href).then(function () {
                                $timeout(function () {
                                    $ionicLoading.show({
                                        template: 'Verifing... Success!'
                                    });
                                    $timeout(function () {
                                        $ionicLoading.hide();
                                    }, 300);
                                }, 100);
                                /*$ionicPopup.alert({
                                    title: 'Success',
                                    template: 'Entity verified. Reload at will.'
                                });*/
                            }, function () {
                                $ionicLoading.hide();
                                $ionicPopup.alert({
                                    title: 'Error',
                                    template: 'Could not verify entity.'
                                });
                            });
                        }, 500);
                    };
                }],
            link: function (scope, element) {
                scope.element = element;
            }
        };
    })

    .directive('gnArticleInfiniteList', function () {
        return {
            scope: {
                articles: '=',
                moreDataCanBeLoaded: '&',
                onInfinite: '&'
            },
            templateUrl: 'templates/article/partials/article_list_infinite.html',
            controller: ['$scope', function ($scope) {

            }]
        };
    })
;
