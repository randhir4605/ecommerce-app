import { useNavigate } from "react-router-dom";
import { useAuthContext } from "../../context/AuthContext";
import FormLayout from "../FormLayout/FormLayout";
import './Profile.css'
import Button from "../Button/Button";

export default function Profile(){
    const {user,logout} = useAuthContext();
    const navigate = useNavigate();
    
    const handleProfileEdit = ()=>{
        navigate("/profile/edit")
    }

    const handleLogout = async (event) =>{
        const response = await fetch(`${process.env.REACT_APP_API_URL}/api/public/auth/logout` , {method: "POST"});
        const result = await response.json();
        if (!response.ok) {
            throw new Error(
                typeof result === "string"
                    ? result
                    : result.message || result.error || `HTTP ${response.status}`
            );
        }
        logout();
        // logout will change the state before navigating to login the profile component will render again
        // but the user is already set to null the profile HTML code will cause error. So put a condition
        // that user is present or not before returning the JSX code.
        navigate("/login");
    }

    // the condition is this 
    if (!user) {
        return <div>Please log in to view your profile.</div>;
    }

    // Support your current API response structure.
    const profile = user.userAccount ?? user;

    //TODO: keep response simple and seperate for user detail not same as logged in details
    //  and align with dummyjson.com , fetch the user data on profile page seperatly

    return (
        <main id="profile-readonly" className="profile-readonly">
            <section>
                <fieldset className="profile-fieldset">
                    <legend>Profile</legend>
                    <div className="profile">
                        <div className="profile-info">
                            <div className="avatar">{profile.fullname.charAt(0)}</div>
                            <div>
                            <h4>{profile.fullname}</h4>
                            <p>@{profile.username}</p>
                            </div>
                        </div>
                    
                    <hr />
                    <div className="profile-details">
                        <div className="profile-row"><span>Name</span><span>{profile.fullname}</span></div>
                        <div className="profile-row"><span>Username</span><span>{profile.username}</span></div>
                        <div className="profile-row"><span>Email</span><span>{profile.email}</span></div>
                        <div className="profile-row"><span>Phone</span><span>{profile.phone}</span></div>
                        <div className="profile-row"><span>Address</span><span>{profile.address}</span></div>
                    </div>
                    
                        <div className="profile-action">
                        <Button onClick={handleProfileEdit}>Edit Profile</Button>
                        <Button onClick={handleLogout} variant="danger" >Logout</Button>
                        </div>
                    
                    </div>
                </fieldset>
            </section>
        </main>
    );
};