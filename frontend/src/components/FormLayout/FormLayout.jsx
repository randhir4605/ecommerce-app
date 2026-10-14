import "./FormLayout.css";

export default function FormLayout({ id, title, onSubmit, children}) {
    return (
        <main className="form-page">
            <form id={id} className="form-layout" onSubmit={onSubmit}>
                <fieldset className="form-fieldset">
                    <legend>{title}</legend>
                    {children}
                </fieldset>
            </form>
        </main>
    );
}