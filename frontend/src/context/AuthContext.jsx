import { createContext, useState, useContext, useEffect } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [authStatus, setAuthStatus] = useState("checking");
    const [authError, setAuthError] = useState(null);

    const authLoading = authStatus === "checking";

    function login(userProfile) {
        setUser(userProfile);
        setAuthError(null);
        setAuthStatus("authenticated");
    }

    async function logout() {
        try {
            const response = await fetch(
                `${process.env.REACT_APP_API_URL}/api/public/auth/logout`,
                {
                    method: "POST",
                    credentials: "include"
                }
            );

            if (!response.ok && response.status !== 401) {
                throw new Error("Unable to log out from the server.");
            }
        } catch (error) {
            setAuthError(error.message);
        } finally {
            // Clear the frontend state even if the request fails.
            setUser(null);
            setAuthStatus("unauthenticated");
        }
    }

    useEffect(() => {
        let cancelled = false;

        async function checkSession() {
            try {
                const response = await fetch(
                    `${process.env.REACT_APP_API_URL}/api/public/auth/me`,
                    { credentials: "include" }
                );

                if (cancelled) return;

                if (response.ok) {
                    const profile = await response.json();

                    if (cancelled) return;

                    setUser(profile);
                    setAuthStatus("authenticated");
                } else if (response.status === 401) {
                    setUser(null);
                    setAuthStatus("unauthenticated");
                } else {
                    setAuthError(
                        `Unable to verify session (${response.status}).`
                    );
                    setAuthStatus("error");
                }
            } catch (error) {
                if (cancelled) return;

                setAuthError("Unable to connect to the authentication server.");
                setAuthStatus("error");
                console.error("Unable to verify session", error);
            }
        }

        checkSession();

        return () => {
            cancelled = true;
        };
    }, []);

    return (
        <AuthContext.Provider value={{ user, setUser, login, logout, authLoading, authStatus, authError }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuthContext() {
    const context = useContext(AuthContext);

    if (!context) {
        throw new Error(
            "useAuthContext must be used inside AuthProvider"
        );
    }

    return context;
}