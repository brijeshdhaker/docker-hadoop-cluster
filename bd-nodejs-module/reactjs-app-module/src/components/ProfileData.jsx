import React from "react";

/**
 * Renders information about the user obtained from MS Graph
 * @param props 
 */
export const ProfileData = (props) => {
    return (
        <div id="profile-div">
            <p><strong>First Name: </strong> { JSON.stringify(props.graphData, null, 2) }</p>
        </div>
    );
};


export const RestData = (props) => {
    return (
        <div id="profile-div">
            <p><strong>Users : </strong> { JSON.stringify(props.restData, null, 2) }</p>
        </div>
    );
};