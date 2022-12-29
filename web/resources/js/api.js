'use strict';

async function apiLogin(email, password) {
    function apiFetch(email, password) {
        let myHeaders = new Headers();
        myHeaders.append("Content-Type", 'application/json');
        myHeaders.append("Content-Api", tokenGenerator(8));

        let raw = JSON.stringify({
            email,
            password
        });

        let requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: raw,
        };
        const response = fetch(`/auth/login`, requestOptions);
        return response.then(res => res.json());
    }

    let result;
    try {
        result = await apiFetch(email, password);
        return result;
    } catch (error) {
        console.log(error);
    }
}
