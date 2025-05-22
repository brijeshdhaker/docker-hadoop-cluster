import React, { useState } from 'react';

import { PageLayout } from './components/PageLayout';
import { loginRequest, apiLoginRequest } from './authConfig';
import { callMsGraph } from './graph';
import { callR1RestApi, callR2RestApi } from './api';
import { ProfileData, RestData  } from './components/ProfileData';

import { AuthenticatedTemplate, UnauthenticatedTemplate, useMsal } from '@azure/msal-react';
import './App.css';
import Button from 'react-bootstrap/Button';

/**
 * Renders information about the signed-in user or a button to retrieve data about the user
 */

const ProfileContent = () => {

    const { instance, accounts } = useMsal();
    const [graphData, setGraphData] = useState(null);

    function RequestProfileData() {
        // Silently acquires an access token which is then attached to a request for MS Graph data
        instance
            .acquireTokenSilent({
                ...loginRequest,
                account: accounts[0],
            })
            .then((response) => {
                console.log("Graph API Token : " + response.accessToken);
                callMsGraph(response.accessToken).then((response) => setGraphData(response));
            });
    }

    function RequestUserData() {
        // Silently acquires an access token which is then attached to a request for Rest API data
        instance
            .acquireTokenSilent({
                ...apiLoginRequest,
                account: accounts[0],
            })
            .then((response) => {
                console.log("Rest API Token : " + response.accessToken);
                callR1RestApi(response.accessToken).then((response) => setGraphData(response));
            });
    }

    return (
        <>
            <h5 className="profileContent">Welcome {accounts[0].name}</h5>
            {graphData ? (
                <ProfileData graphData={graphData} />
            ) : (
                <>
                <Button variant="secondary" onClick={RequestProfileData}>Request Profile</Button>
                <Button variant="primary" onClick={RequestUserData}>Request Users</Button>
                </>
            )}
        </>
    );
};

/**
 * If a user is authenticated the ProfileContent component above is rendered. Otherwise a message indicating a user is not authenticated is rendered.
 */
const MainContent = () => {
    return (
        <div className="App">
            <AuthenticatedTemplate>
                <ProfileContent />
            </AuthenticatedTemplate>

            <UnauthenticatedTemplate>
                <h5 className="card-title">Please sign-in to see your profile information.</h5>
            </UnauthenticatedTemplate>
        </div>
    );
};

export default function App() {
    return (
        <PageLayout>
            <MainContent />
        </PageLayout>
    );
}
