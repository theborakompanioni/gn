// Ionic Starter App


angular.module('ngLodash', [])
    .constant('lodash', null)
    .config(['$provide', function ($provide) {
        $provide.constant('_', _);
    }]);

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.controllers' is found in controllers.js
angular.module('starter', [
    'ionic',
    'ionic-material',
    'ionMdInput',
    'LocalForageModule',
    'ngLodash',
    'starter.controllers',
    'starter.directives'
])

    .run(function ($ionicPlatform) {
        $ionicPlatform.ready(function () {
            // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
            // for form inputs)
            if (window.cordova && window.cordova.plugins.Keyboard) {
                cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
            }
            if (window.StatusBar) {
                // org.apache.cordova.statusbar required
                StatusBar.styleDefault();
            }
        });
    })

    .config(function ($stateProvider, $urlRouterProvider, $ionicConfigProvider) {

        // Turn off caching for demo simplicity's sake
        $ionicConfigProvider.views.maxCache(0);

        /*
         // Turn off back button text
         $ionicConfigProvider.backButton.previousTitleText(false);
         */

        $stateProvider.state('app', {
            url: '/app',
            abstract: true,
            templateUrl: 'templates/menu.html',
            controller: 'AppCtrl',
            resolve: {
                '$gnApi': ['$http', function ($http) {
                    return $http.get('/api').then(function (response) {
                        return response.data;
                    });
                }]
            }
        })
            .state('app.activity', {
                url: '/activity',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/activity.html',
                        controller: 'ActivityCtrl'
                    },
                    'fabContent': {
                        template: '<button id="fab-activity" class="button button-fab button-fab-top-right expanded button-energized-900 flap"><i class="icon ion-paper-airplane"></i></button>',
                        controller: function ($timeout) {
                            $timeout(function () {
                                document.getElementById('fab-activity').classList.toggle('on');
                            }, 200);
                        }
                    }
                }
            })

            .state('app.friends', {
                url: '/friends',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/friends.html',
                        controller: 'FriendsCtrl'
                    },
                    'fabContent': {
                        template: '<button id="fab-friends" class="button button-fab button-fab-top-left expanded button-energized-900 spin"><i class="icon ion-chatbubbles"></i></button>',
                        controller: function ($timeout) {
                            $timeout(function () {
                                document.getElementById('fab-friends').classList.toggle('on');
                            }, 900);
                        }
                    }
                }
            })

            .state('app.gallery', {
                url: '/gallery',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/gallery.html',
                        controller: 'GalleryCtrl'
                    },
                    'fabContent': {
                        template: '<button id="fab-gallery" class="button button-fab button-fab-top-right expanded button-energized-900 drop"><i class="icon ion-heart"></i></button>',
                        controller: function ($timeout) {
                            $timeout(function () {
                                document.getElementById('fab-gallery').classList.toggle('on');
                            }, 600);
                        }
                    }
                }
            })

            .state('app.login', {
                url: '/login',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/login.html',
                        controller: 'LoginCtrl'
                    },
                    'fabContent': {
                        template: ''
                    }
                }
            })

            .state('app.profile', {
                url: '/profile',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/profile.html',
                        controller: 'ProfileCtrl'
                    },
                    'fabContent': {
                        template: '<button id="fab-profile" class="button button-fab button-fab-bottom-right button-energized-900"><i class="icon ion-plus"></i></button>',
                        controller: function ($scope, $state) {
                        }
                    }
                }
            })
            .state('app.articles', {
                url: '/articles',
                resolve: {
                    '$gnArticleSortMode': ['$stateParams', function ($stateParams) {
                        return 'newest';
                    }]
                },
                views: {
                    'menuContent': {
                        templateUrl: 'templates/article/list.html',
                        controller: 'ArticleCtrl'
                    },
                    'fabContent': {
                        template: '<button id="fab-profile" data-ng-click="onClick()" class="button button-fab button-fab-top-right button-energized-900 drop"><i class="icon ion-plus"></i></button>',
                        controller: function ($scope, $state, $timeout) {
                            $scope.onClick = function () {
                                $state.go('app.articlecreate');
                            };
                            $timeout(function () {
                                document.getElementById('fab-profile').classList.toggle('on');
                            }, 600);
                        }
                    }
                }
            })
            .state('app.articlespopular', {
                url: '/popular',
                resolve: {
                    '$gnArticleSortMode': function () {
                        return 'popular';
                    }
                },
                views: {
                    'menuContent': {
                        templateUrl: 'templates/article/list.html',
                        controller: 'ArticleCtrl'
                    },
                    'fabContent': {
                        template: '<button id="fab-profile" data-ng-click="onClick()" class="button button-fab button-fab-top-right button-energized-900 drop"><i class="icon ion-plus"></i></button>',
                        controller: function ($scope, $state, $timeout) {
                            $scope.onClick = function () {
                                $state.go('app.articlecreate');
                            };
                            $timeout(function () {
                                document.getElementById('fab-profile').classList.toggle('on');
                            }, 600);
                        }
                    }
                }
            })
            .state('app.articlestrending', {
                url: '/trending',
                resolve: {
                    '$gnArticleSortMode': function () {
                        return 'trending';
                    }
                },
                views: {
                    'menuContent': {
                        templateUrl: 'templates/article/list.html',
                        controller: 'ArticleCtrl'
                    },
                    'fabContent': {
                        template: '<button id="fab-profile" data-ng-click="onClick()" class="button button-fab button-fab-top-right button-energized-900 drop"><i class="icon ion-plus"></i></button>',
                        controller: function ($scope, $state, $timeout) {
                            $scope.onClick = function () {
                                $state.go('app.articlecreate');
                            };
                            $timeout(function () {
                                document.getElementById('fab-profile').classList.toggle('on');
                            }, 600);
                        }
                    }
                }
            })
            .state('app.articlesverification', {
                url: '/verification',
                resolve: {
                    '$gnArticleSortMode': function () {
                        return 'verification';
                    }
                },
                views: {
                    'menuContent': {
                        templateUrl: 'templates/article/verification.html',
                        controller: 'ArticleVerificationCtrl'
                    },
                    'fabContent': {
                        template: '<button id="fab-profile" data-ng-click="onClick()" class="button button-fab button-fab-top-right button-energized-900 drop"><i class="icon ion-plus"></i></button>',
                        controller: function ($scope, $state, $timeout) {
                            $scope.onClick = function () {
                                $state.go('app.articlecreate');
                            };
                            $timeout(function () {
                                document.getElementById('fab-profile').classList.toggle('on');
                            }, 600);
                        }
                    }
                }
            })
            .state('app.articlecreate', {
                url: '/create',
                views: {
                    'menuContent': {
                        templateUrl: 'templates/article/create.html',
                        controller: 'ArticleCreateCtrl'
                    }
                },
                'fabContent': {
                    template: '',
                    controller: function ($scope, $state) {
                    }
                }
            })
        ;

        // if none of the above states are matched, use this as the fallback
        $urlRouterProvider.otherwise('/app/articles');
    });
