import "./Button.css"

export default function Button({type="button", onClick, variant="primary", size, disabled = false, icon, children}){

    return(
        <button type={type} onClick={onClick} disabled={disabled} className={`app-button app-button--${variant} app-button--${size}`}>
            {icon && <span className="app-button__icon" aria-hidden="true">{icon}</span>}
            {children}
        </button>
    );
}