import React from "react";

/**
 * Renders information about the user obtained from MS Graph
 * @param props 
 */
export const ProfileData = (props) => {
    return (
        <div id="profile-div">
            <p><strong>First Name: </strong> {props.graphData}</p>
            
        </div>
    );
};
