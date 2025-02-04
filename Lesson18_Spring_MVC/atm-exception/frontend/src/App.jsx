import { useState } from "react";
import PinScreen from "./components/PinScreen";
import TerminalScreen from "./components/TerminalScreen";

function App() {
    const [authenticated, setAuthenticated] = useState(false);

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            {!authenticated ? (
                <PinScreen onAuthSuccess={() => setAuthenticated(true)} />
            ) : (
                <TerminalScreen />
            )}
        </div>
    );
}

export default App;