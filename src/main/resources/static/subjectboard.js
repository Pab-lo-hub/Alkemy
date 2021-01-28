var app = new Vue({
    el: '#app',
    data: {
        datos: null,
        subjects: null,
        dni: "",
        legajo: "",
        postdone: null,
        rol: null,
    },
    methods: {
        joinSubject: function (subjectId) {
            $.post('api/subjects/' + subjectId + '/users').done(function () {
                alert("Inscripto");
            })
        },
    },
    filters: {
        capitalize: function (value) {
            if (!value) return ''
            return moment(value).format('DD/MM/YYYY HH:mm:ss A');
        }
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
        app.subjects = json.subjects;
        app.rol = json.user.rol;
        console.log(json);
    }).catch(function (error) {
        console.log("Request failed: " + error.message);
    });