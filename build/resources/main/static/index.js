var app = new Vue({
    el: '#app',
    data: {
        datos: null,
        dni: "",
        legajo: "",
        rol: "",
        picked: "",
    },
    methods: {
        login: function () {
            if (app.dni.length != 0 && app.legajo.length != 0) {
                $.post("/api/login", {
                        dni: app.dni,
                        legajo: app.legajo
                    }).done(function () {
                        if (app.picked == "usuario") {
                            alert("Estudiante logeado")
                            location.assign("/subjectboard.html");
                        }
                        else  {
                            alert("Admin logeado")
                            location.assign("/administration.html");
                        }
                    })
                    .catch(function (error) {
                        if (error.status == 401)
                            alert("usurio o clave invalidos")
                        else
                            alert("Ocurrio un error, contacte al administrador")
                    })
                    .catch(function (error) {
                        alert(error.responseText)
                    })
            } else {
                alert("Datos no válidos. Volver a ingresar")
            }
        },
    },
});

fetch("/api/subjects", {
        method: 'GET',
        headers: {}
    })
    .then(function (response) {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error(response.statusText)
        };
    })
    .then(function (json) {
        app.datos = json;
        console.log(json);
    }).catch(function (error) {
        console.log("Request failed: " + error.message);
    });