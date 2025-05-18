/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

const { LogLevel } = require("@azure/msal-node");

/**
 * Configuration object to be passed to MSAL instance on creation.
 * For a full list of MSAL.js configuration parameters, visit:
 * https://github.com/AzureAD/microsoft-authentication-library-for-js/blob/dev/lib/msal-node/docs/configuration.md
 */
const AAD_ENDPOINT_HOST = "https://login.microsoftonline.com"; // include the trailing slash

const msalConfig = {
    auth: {
        clientId: "c5c062d8-4fe2-4319-9897-a59e57ed7ad2",
        authority: `${AAD_ENDPOINT_HOST}/da5ac8f7-13d6-46e7-815d-012b01123148`,
    },
    system: {
        loggerOptions: {
            loggerCallback(loglevel, message, containsPii) {
                console.log(message);
            },
            piiLoggingEnabled: false,
            logLevel: LogLevel.Verbose,
        },
    },
};

/**
 * Add here the endpoints and scopes when obtaining an access token for protected web APIs. For more information, see:
 * https://github.com/AzureAD/microsoft-authentication-library-for-js/blob/dev/lib/msal-browser/docs/resources-and-scopes.md
 */
const GRAPH_ENDPOINT_HOST = "https://graph.microsoft.com"; // include the trailing slash

const protectedResources = {
    graphMe: {
        endpoint: `${GRAPH_ENDPOINT_HOST}/v1.0/me`,
        scopes: ["User.Read"],
    }
};


module.exports = {
    msalConfig: msalConfig,
    protectedResources: protectedResources,
};
