package tokoberkah;

public class Main {
    public static void main(String[] args) {
        // Menggunakan java.awt.EventQueue agar GUI berjalan lebih stabil (Thread-Safe)
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Membuat objek dari class Login
                Login loginForm = new Login();
                
                // Memunculkan jendela Login ke layar
                loginForm.setVisible(true); 
            }
        });
    }
}