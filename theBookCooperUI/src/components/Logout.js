import useUser from "../hooks/useUser";
import { useNavigate } from "react-router-dom";
import {getAuth, signOut} from 'firebase/auth';
import '../styles/Login.css';
import { useDetails } from '../hooks/useDetails';

const Logout = () => {
    
    //for some reason you NEED to have isLoading (even though we don't use it)
    //or else the log in/log out buttons WILL NOT WORK (????)

    const { user, isLoading } = useUser();
    const navigate = useNavigate();
    const { userId, setUserId } = useDetails();

    const handleLogout = () => {
        const auth = getAuth();
        signOut(auth)
            .then(() => {
                localStorage.removeItem('userId');
                setUserId(null);
                console.log("After logging out userId is: " + userId)
                navigate('/');
            })
            .catch((e) => {
                console.error("Logout failed", e);
            });
    };

    return (
        
        <div className="header-buttons">
            {user
                ? <button className = "login-button" onClick={handleLogout}>Log Out</button>
                : <button className = "login-button" onClick={() => navigate('/login')}>Log In</button>
            }
        </div>
    );
};

export default Logout;
