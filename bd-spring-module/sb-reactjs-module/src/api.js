import { restConfig } from "./authConfig";

/**
 * Attaches a given access token to a MS Graph API call. Returns information about the user
 * @param accessToken 
 */
export async function callR1RestApi(accessToken) {
    const headers = new Headers();
    const bearer = `Bearer ${accessToken}`;

    headers.append("Authorization", bearer);
    headers.append("X-Tenant-Id", "DC-R1");
    headers.append("Access-Control-Allow-Origin", "*");

    const options = {
        method: "GET",
        headers: headers
    };

    return fetch(restConfig.restEndpoint, options)
        .then(response => response.json())
        .catch(error => console.log(error));
}

/**
 * Attaches a given access token to a MS Graph API call. Returns information about the user
 * @param accessToken 
 */
export async function callR2RestApi(accessToken) {
    const headers = new Headers();
    const bearer = `Bearer ${accessToken}`;

    headers.append("Authorization", bearer);
    headers.append("X-Tenant-Id", "DC-R1");

    const options = {
        method: "GET",
        headers: headers
    };

    return fetch(restConfig.restEndpoint, options)
        .then(response => response.json())
        .catch(error => console.log(error));
}