/* global angular, document, window */
'use strict';

angular.module('starter.controllers', ['ngLodash'])

    .controller('AppCtrl', function ($scope, $window, $http, $timeout, $state, $ionicModal, $ionicPopover, $ionicLoading, $gnApi) {
        // Form data for the login modal
        $scope.loginData = {};
        $scope.isExpanded = false;
        $scope.hasHeaderFabLeft = false;
        $scope.hasHeaderFabRight = false;

        $scope.gnApi = $gnApi;

        var navIcons = document.getElementsByClassName('ion-navicon');
        for (var i = 0; i < navIcons.length; i++) {
            navIcons.addEventListener('click', function () {
                this.classList.toggle('active');
            });
        }

        ////////////////////////////////////////
        // Layout Methods
        ////////////////////////////////////////

        $scope.hideNavBar = function () {
            document.getElementsByTagName('ion-nav-bar')[0].style.display = 'none';
        };

        $scope.showNavBar = function () {
            document.getElementsByTagName('ion-nav-bar')[0].style.display = 'block';
        };

        $scope.noHeader = function () {
            var content = document.getElementsByTagName('ion-content');
            for (var i = 0; i < content.length; i++) {
                if (content[i].classList.contains('has-header')) {
                    content[i].classList.toggle('has-header');
                }
            }
        };

        $scope.setExpanded = function (bool) {
            $scope.isExpanded = bool;
        };

        $scope.setHeaderFab = function (location) {
            var hasHeaderFabLeft = false;
            var hasHeaderFabRight = false;

            switch (location) {
                case 'left':
                    hasHeaderFabLeft = true;
                    break;
                case 'right':
                    hasHeaderFabRight = true;
                    break;
            }

            $scope.hasHeaderFabLeft = hasHeaderFabLeft;
            $scope.hasHeaderFabRight = hasHeaderFabRight;
        };

        $scope.hasHeader = function () {
            var content = document.getElementsByTagName('ion-content');
            for (var i = 0; i < content.length; i++) {
                if (!content[i].classList.contains('has-header')) {
                    content[i].classList.toggle('has-header');
                }
            }

        };

        $scope.hideHeader = function () {
            $scope.hideNavBar();
            $scope.noHeader();
        };

        $scope.showHeader = function () {
            $scope.showNavBar();
            $scope.hasHeader();
        };

        $scope.clearFabs = function () {
            var fabs = document.getElementsByClassName('button-fab');
            if (fabs.length && fabs.length > 1) {
                fabs[0].remove();
            }
        };

        $scope.logout = function () {
            $ionicLoading.show({
                template: 'Logging out...'
            });

            $timeout(function () {
                $http.post($gnApi._links.logout.href).then(function () {
                    $ionicLoading.hide();
                    $window.location.reload();
                }, function () {
                    $ionicLoading.hide();
                    $ionicPopup.alert({
                        title: 'Authorization Error',
                        template: 'Logout has not been successful.'
                    });
                });
            }, 500);
        };
    })

    .controller('LoginCtrl', function ($scope, $window, $timeout, $state, $stateParams,
                                       $ionicLoading, $ionicPopup, $http, $gnApi, ionicMaterialInk) {
        $scope.$parent.clearFabs();
        $timeout(function () {
            $scope.$parent.showHeader();
        }, 0);
        ionicMaterialInk.displayEffect();

        $scope.model = {
            usernameInput: '',
            usernamePassword: ''
        };

        $scope.submit = function () {

            $ionicLoading.show({
                template: 'Authenticating...'
            });

            var token = {
                username: $scope.model.usernameInput,
                password: $scope.model.passwordInput
            };


            $timeout(function () {
                $http.post($gnApi._links.login.href, token).then(function () {
                    $ionicLoading.hide();

                    $state.go('app.articles').then(function () {
                        // TODO: workaround to load $gnApi request again.. Better idea, anyone?
                        $timeout(function () {
                            $window.location.reload();
                        }, 1);
                    });
                }, function () {
                    $ionicLoading.hide();
                    $ionicPopup.alert({
                        title: 'Authorization Error',
                        template: 'Invalid Credentials provided.'
                    });
                });
            }, 500);
        };
    })

    .controller('FriendsCtrl', function ($scope, $stateParams, $timeout, ionicMaterialInk, ionicMaterialMotion) {
        // Set Header
        $scope.$parent.showHeader();
        $scope.$parent.clearFabs();
        $scope.$parent.setHeaderFab('left');

        // Delay expansion
        $timeout(function () {
            $scope.isExpanded = true;
            $scope.$parent.setExpanded(true);
        }, 300);

        // Set Motion
        ionicMaterialMotion.fadeSlideInRight();

        // Set Ink
        ionicMaterialInk.displayEffect();
    })

    .controller('ProfileCtrl', function ($scope, $stateParams, $timeout, ionicMaterialMotion, ionicMaterialInk) {
        // Set Header
        $scope.$parent.showHeader();
        $scope.$parent.clearFabs();
        $scope.isExpanded = false;
        $scope.$parent.setExpanded(false);
        $scope.$parent.setHeaderFab(false);

        // Set Motion
        $timeout(function () {
            ionicMaterialMotion.slideUp({
                selector: '.slide-up'
            });
        }, 300);

        $timeout(function () {
            ionicMaterialMotion.fadeSlideInRight({
                startVelocity: 3000
            });
        }, 700);

        // Set Ink
        ionicMaterialInk.displayEffect();
    })
    .controller('ArticleCtrl', function ($scope, $stateParams, $timeout, $http, $gnApi,
                                         $gnArticleSortMode, $ionicPopup, $ionicLoading,
                                         ionicMaterialMotion, ionicMaterialInk) {
        // Set Header
        $scope.$parent.showHeader();
        $scope.$parent.clearFabs();
        $scope.isExpanded = false;
        $scope.$parent.setExpanded(false);
        $scope.$parent.setHeaderFab(false);

        $scope.sortMode = $gnArticleSortMode || 'newest';

        // Set Ink
        ionicMaterialInk.displayEffect();

        var fetchArticles = function () {
            var pageParams = {
                page: 0,
                size: 100
            };
            return $http.get($gnApi._links[$scope.sortMode].href, {params: pageParams});
        };

        $scope.doRefresh = function () {
            $scope.data = null;
            $scope.articles = [];
            fetchArticles().then(function (response) {
                $scope.data = response.data;
                $scope.articles = $scope.data.content;

                $timeout(function () {
                    ionicMaterialMotion.fadeSlideInRight({
                        startVelocity: 3000
                    });
                }, 10);
            })['finally'](function () {
                $scope.$broadcast('scroll.refreshComplete');
            });
        };

        (function init() {
            $scope.doRefresh();
        })();

        $scope.moreDataCanBeLoaded = function () {
            return $scope.data && $scope.articles.length < $scope.data.totalElements;
        };

        $scope.onInfiniteCallback = function () {
            if ($scope.data && $scope.articles.length >= $scope.data.totalElements) {
                $scope.$broadcast('scroll.infiniteScrollComplete');
                return;
            }
            var pageParams = function () {
                return {
                    page: $scope.data.number + 1,
                    size: 100
                };
            };

            $http.get($gnApi._links[$scope.sortMode].href, {params: pageParams()}).then(function success(response) {
                $scope.data = response.data;
                $scope.articles = $scope.articles.concat($scope.data.content);

                $timeout(function () {
                    ionicMaterialMotion.fadeSlideInRight({
                        startVelocity: 3000
                    });

                    $scope.$broadcast('scroll.infiniteScrollComplete');
                }, 10);
            }, function error() {
                $scope.$broadcast('scroll.infiniteScrollComplete');
            });
        };
    })

    .controller('ArticleVerificationCtrl', function ($scope, $stateParams, $timeout, $http, $gnApi,
                                                     $ionicPopup, $ionicLoading,
                                                     ionicMaterialMotion, ionicMaterialInk) {
        // Set Header
        $scope.$parent.showHeader();
        $scope.$parent.clearFabs();
        $scope.isExpanded = false;
        $scope.$parent.setExpanded(false);
        $scope.$parent.setHeaderFab(false);

        $scope.articles = [];
        $scope.data = null;
        $scope.sort = {
            proprety: null,
            reverse: false
        };

        // Set Ink
        ionicMaterialInk.displayEffect();

        var fetchArticles = function () {
            var getSortParam = function () {
                var sort = $scope.sort.proprety || 'createdAt';
                if (sort) {
                    sort += ','
                }
                sort += $scope.sort.reverse ? 'asc' : 'desc';
                return sort;
            };
            var pargeNumber = ($scope.data && $scope.data.number || -1) + 1;

            var pageParams = function () {
                return {
                    page: pargeNumber,
                    size: 100,
                    sort: getSortParam()
                };
            };

            return $http.get($gnApi._links.verification.href, {params: pageParams()});
        };

        $scope.doRefresh = function () {
            $scope.data = null;
            $scope.articles = [];
            fetchArticles().then(function (response) {
                $scope.data = response.data;
                $scope.articles = $scope.data.content;

                $timeout(function () {
                    ionicMaterialMotion.fadeSlideInRight({
                        startVelocity: 3000
                    });
                }, 10);
            })['finally'](function () {
                $scope.$broadcast('scroll.refreshComplete');
            });
        };

        (function init() {
            $scope.doRefresh();
        })();

        $scope.onFormChange = function () {
            $scope.doRefresh();
        };

        $scope.moreDataCanBeLoaded = function () {
            return $scope.data && $scope.articles.length < $scope.data.totalElements;
        };

        $scope.onInfiniteCallback = function () {
            if ($scope.data && $scope.articles.length >= $scope.data.totalElements) {
                $scope.$broadcast('scroll.infiniteScrollComplete');
                return;
            }

            fetchArticles().then(function success(response) {
                $scope.data = response.data;
                $scope.articles = $scope.articles.concat($scope.data.content);

                $timeout(function () {
                    ionicMaterialMotion.fadeSlideInRight({
                        startVelocity: 3000
                    });

                    $scope.$broadcast('scroll.infiniteScrollComplete');
                }, 10);
            }, function error() {
                $scope.$broadcast('scroll.infiniteScrollComplete');
            });
        };
    })
    .controller('ArticleCreateCtrl', function ($scope, $stateParams, $state, $timeout, $http,
                                               $ionicLoading, $ionicPopup, ionicMaterialMotion,
                                               ionicMaterialInk) {
        $scope.$parent.showHeader();
        $scope.$parent.clearFabs();
        $scope.isExpanded = false;
        $scope.$parent.setExpanded(false);
        $scope.$parent.setHeaderFab(false);

        $scope.article = {};

        $scope.submit = function () {
            $ionicLoading.show({
                template: 'Creating...'
            });
            var article = angular.copy($scope.article);

            $http.post('/articles', article)
                .then(function success() {
                    $scope.clearForm();
                    $timeout(function () {
                        $ionicLoading.show({
                            template: 'Creating... Success!'
                        });
                        $timeout(function () {
                            $state.go('app.articlesverification');
                            $timeout(function () {
                                $ionicLoading.hide();
                            }, 1000);
                        }, 800);
                    }, 200);
                }, function error() {
                    $ionicLoading.hide();
                    $ionicPopup.alert({
                        title: 'Error',
                        template: 'Article could not be created.'
                    });
                });
        };

        $scope.clearForm = function () {
            $scope.article = {};
        };
        // Set Ink
        ionicMaterialInk.displayEffect();
    })
    .controller('ActivityCtrl', function ($scope, $stateParams, $timeout, ionicMaterialMotion, ionicMaterialInk) {
        $scope.$parent.showHeader();
        $scope.$parent.clearFabs();
        $scope.isExpanded = true;
        $scope.$parent.setExpanded(true);
        $scope.$parent.setHeaderFab(false);

        $timeout(function () {
            ionicMaterialMotion.fadeSlideIn({
                selector: '.animate-fade-slide-in .item'
            });
        }, 200);

        // Activate ink for controller
        ionicMaterialInk.displayEffect();
    })

    .controller('GalleryCtrl', function ($scope, $stateParams, $timeout, ionicMaterialInk, ionicMaterialMotion) {
        $scope.$parent.showHeader();
        $scope.$parent.clearFabs();
        $scope.isExpanded = true;
        $scope.$parent.setExpanded(true);
        $scope.$parent.setHeaderFab(false);

        // Activate ink for controller
        ionicMaterialInk.displayEffect();

        ionicMaterialMotion.pushDown({
            selector: '.push-down'
        });
        ionicMaterialMotion.fadeSlideInRight({
            selector: '.animate-fade-slide-in .item'
        });

    })


    .controller('AboutCtrl', function ($scope, $stateParams, $timeout, ionicMaterialInk, ionicMaterialMotion) {
        $scope.$parent.showHeader();
        $scope.$parent.clearFabs();
        $scope.isExpanded = false;
        $scope.$parent.setExpanded(false);
        $scope.$parent.setHeaderFab(false);

        // Activate ink for controller
        ionicMaterialInk.displayEffect();

        ionicMaterialMotion.pushDown({
            selector: '.push-down'
        });
        ionicMaterialMotion.fadeSlideInRight({
            selector: '.animate-fade-slide-in .item'
        });

    })
;
