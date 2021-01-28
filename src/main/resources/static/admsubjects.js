var app = new Vue({
    el: '#app',
    data: {
        datos: null,
        subjects: null,
        dni: "",
        legajo: "",
        rol: "",
        picked: "",
        professors: "",
        rol: null,
    },
    methods: {},
    filters: {
        capitalize: function (value) {
            if (!value) return ''
            return moment(value).format('DD/MM/YYYY HH:mm:ss A');
        }
    },
});

fetch("/api/administrators", {
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
        app.professors = json.professors;
        console.log(json)
    }).catch(function (error) {
        console.log("Request failed: " + error.message);
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
    .then(function (data) {
        app.datos = data;
        app.subjects = data.subjects;
        app.rol = data.user.rol;
        console.log(data);
    }).catch(function (error) {
        console.log("Request failed: " + error.message);
    });