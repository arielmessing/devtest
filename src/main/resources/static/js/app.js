(function () {

    var app = angular.module('notesApp', ['ngRoute', 'ngMaterial']);

    app.config(['$locationProvider', '$routeProvider',
        function ($locationProvider, $routeProvider) {

            $routeProvider
                .when('/', {
                    templateUrl: '/partials/notes-view.html',
                    controller: 'notesController'
                })
                .when('/login', {
                    templateUrl: '/partials/login.html',
                    controller: 'loginController',
                })
                .otherwise('/');
        }
    ]);

    app.run(['$rootScope', '$location', 'AuthService', 'NotesService',
        function ($rootScope, $location, AuthService, NotesService) {
            $rootScope.$on('$routeChangeStart', function (event) {

                if ($location.path() == "/login") {
                    return;
                }

                if (!AuthService.isLoggedIn()) {
                    console.log('DENY');
                    event.preventDefault();
                    $location.path('/login');
                }
            });
        }]);


    app.service('AuthService', function ($http) {
        let loggedUser = null;
        
        loadLoggedInUser();

        function login(username, password) {
            return $http.post("api/login", { username: username, password: password }).then(function (user) {
                loggedUser = user;

                saveLoggedInUser(user);

            }, function (error) {
                loggedUser = null;
            })
        }

        function isLoggedIn() {
            return loggedUser != null;
        }

        function logout() {
            loggedUser = null;

            saveLoggedInUser(null);
        }

        function saveLoggedInUser(user) {
            sessionStorage.loggedInUser = angular.toJson(user);
        }

        function loadLoggedInUser() {
            if (sessionStorage.loggedInUser) {
                loggedUser = angular.fromJson(sessionStorage.loggedInUser);
            }
        }

        return {
            login: login,
            isLoggedIn: isLoggedIn,
            logout: logout
        }
    });

    app.service('NotesService', function ($http) {

        function getNotes() {
            return $http.get('api/notes/').then(function (res) {
                if (!res.data.error) {
                    notes = res.data;
                    return notes;
                }

            }, function (error) {
            });
        }

        function getNote(id) {
            let url = `api/notes/${id}/`;

            return $http.get(url).then(function (res) {
                if (!res.data.error) {
                    return res.data;
                }

            }, function (error) {
            });
        }
        
        function saveNote(note) {
            let id = note.id ? note.id : 'new';
            let url = `api/notes/${id}/`;

            return $http.post(url, note).then(function (res) {

            }, function (error) {
            });
        }

        return {
            getNotes: getNotes,
            getNote: getNote,
            saveNote: saveNote
        }
    });

    app.controller('headerController', function ($scope, AuthService, $location) {
        $scope.logout = function () {
            AuthService.logout();

            $location.path('/login');
        }
    });

    app.controller('loginController', function ($scope, AuthService, $location) {

        $scope.invalidCreds = false;
        $scope.credentials = {
            username: null,
            password: null
        };

        $scope.isValid = function () {
            // TODO: Use built-in form validation + submit?

            return (
                $scope.credentials.username &&
                $scope.credentials.username.trim().length > 0 &&
                $scope.credentials.password &&
                $scope.credentials.password.trim().length > 0);
        };

        $scope.login = function () {
            AuthService.login($scope.credentials.username, $scope.credentials.password).then(
                function (user) {
                    console.log(user);

                    if (AuthService.isLoggedIn()) {
                        $location.path("/");

                    } else {
                        $scope.invalidCreds = true;
                    }

                }, function (error) {
                    console.log(error);
                    $scope.invalidCreds = true;
                });
        };
    });

    app.controller('notesController', function ($scope, NotesService) {

        $scope.notes = [];
        $scope.currentNote = null;
        $scope.isEditCreateView = false;

        $scope.restrictions = {
            MAX_NAME: 10,
            MAX_TEXT: 500
        }

        getNotes();

        $scope.newNoteView = function () {

            $scope.currentNote = {
                id: null,
                timstamp: null,
                name: null,
                text: null
            };

            $scope.isEditCreateView = true;
        };

        $scope.isCurrentNoteValid = function () {
            return (
                $scope.currentNote.name &&
                $scope.currentNote.name.length <= $scope.restrictions.MAX_NAME && 
                $scope.currentNote.text &&
                $scope.currentNote.text.length <= $scope.restrictions.MAX_TEXT);
        };

        $scope.deleteNote = function (i) {
            var r = confirm("Are you sure you want to delete this note?");
            if (r == true) {
                //TODO delete the note
            }
        };

        $scope.viewNote = function (id) {
            NotesService.getNote(id).then(function (note) {
                $scope.currentNote = note;

                $scope.isEditCreateView = true;
            })
        };

        $scope.save = function () {
            console.log($scope.currentNote)
            NotesService.saveNote($scope.currentNote).then(function () {

                // TODO: Error handling

                getNotes();

                $scope.isEditCreateView = false;
            });
        }

        $scope.cancel = function () {
            $scope.currentNote = null;

            $scope.isEditCreateView = false;
        }

        function getNotes() {
            NotesService.getNotes().then(function (notes) {
                $scope.notes = notes;
            });
        }
    });

})();