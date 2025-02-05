import {useState} from "react";

import {TerminalScreen} from "./components/TerminalScreen";
import {PinScreen} from "./components/PinScreen.jsx";

function App() {
    const [authenticated, setAuthenticated] = useState(false);
    const [card, setCard] = useState("");  // ✅ Храним карту


    const handleAuthSuccess = (userCard) => {
        setAuthenticated(true);
        setCard(userCard);  // ✅ Сохраняем карту
    };

    return (
        <>
            {!authenticated ? (
                <PinScreen onAuthSuccess={handleAuthSuccess} />  // ✅ Передаем функцию с картой
            ) : (
                <TerminalScreen card={card} />  // ✅ Передаем карту в TerminalScreen
            )}
        </>
    );
}

export default App;