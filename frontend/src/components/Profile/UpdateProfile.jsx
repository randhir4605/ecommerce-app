import FormLayout from "../FormLayout/FormLayout";
import Button from "../Button/Button";
import { useAuthContext } from "../../context/AuthContext";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function UpdateProfile(){

    const userProfile = useAuthContext().user.userAccount;
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        username:"",
        fullname:"",
        password:"",
        phone:"",
        address:""
    });
    useEffect(()=>{
        if(userProfile){
            setFormData({
                username: userProfile.username ?? "",
                fullname: userProfile.fullname ?? "",
                password: userProfile.password ?? "",
                phone: userProfile.phone ?? "",
                address: userProfile.address ?? ""
            });
        }
    },[userProfile]);

    const handleChange = (e) => {
        setFormData({...formData,[e.target.name]: e.target.value});
    }
    const handleUpdateProfile = (e) => {
        e.preventDefault();
        alert("Profile update - under development!")
        navigate("/profile");
    }
    return (
        <FormLayout id="profile-edit" title="Update Profile" onSubmit={handleUpdateProfile}>
            <input id="username" name="username" type="text" placeholder="username" value={formData.username} onChange={handleChange}/>
            <input name="password" id="password" type="text" placeholder="Password" value={formData.password} onChange={handleChange}/>
            <input name="fullname" id="fullname" type="text" placeholder="Full Name" value={formData.fullname} onChange={handleChange}/>
            <input name="phone" id="phone" type="tel" placeholder="Mobile Number" value={formData.phone} onChange={handleChange}/>
            <input name="address" id="address" type="text" placeholder="Address" value={formData.address} onChange={handleChange}/>
            <Button size="large" type="submit">Update Profile</Button>
        </FormLayout>
    );
}