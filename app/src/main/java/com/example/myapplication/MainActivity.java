package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private View loginContainer;
    private View listviewContainer;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private TextView texto;
    private RadioButton radioButton_pulsado;
    private Button backToLoginButton;

    // Clave para guardar y restaurar el estado
    private static final String STATE_IS_GUEST_SESSION = "isGuestSession";
    private static final String STATE_IS_LIST_VISIBLE = "isListVisible";

    // Variable para rastrear la sesión de invitado
    private boolean isGuestSession = false;


    @SuppressLint({"SetTextI18n", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginContainer = findViewById(R.id.login_view_container);
        listviewContainer = findViewById(R.id.listview_container);

        usernameEditText = findViewById(R.id.editText_username);
        passwordEditText = findViewById(R.id.editText_password);
        Button loginButton = findViewById(R.id.button_login);
        Button registerButton = findViewById(R.id.button_register);

        loginButton.setOnClickListener(v -> attemptLogin());

        // LÓGICA DE INVITADO
        registerButton.setOnClickListener(v -> {
            isGuestSession = true;
            Toast.makeText(this, "Acceso en Modo Invitado", Toast.LENGTH_SHORT).show();
            setupListView(); // Refrescar vista con restricción de invitado
            switchToListView();
        });

        // Llamar a setupListView antes de restaurar el estado para inicializar el adaptador
        setupListView();

        // ----------------------------------------------------
        // CAMBIO 1: Lógica de restauración de estado (maneja rotación y recreación)
        if (savedInstanceState != null) {
            isGuestSession = savedInstanceState.getBoolean(STATE_IS_GUEST_SESSION, false);
            boolean isListVisible = savedInstanceState.getBoolean(STATE_IS_LIST_VISIBLE, false);

            if (isListVisible) {
                switchToListView();
            } else {
                loginContainer.setVisibility(View.VISIBLE);
                listviewContainer.setVisibility(View.GONE);
            }
        } else {
            // Estado inicial si no hay estado guardado (primera ejecución)
            listviewContainer.setVisibility(View.GONE);
            loginContainer.setVisibility(View.VISIBLE);
        }
        // ----------------------------------------------------
    }

    // ----------------------------------------------------
    // CAMBIO 2: Guardar el estado de la vista al rotar
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_IS_GUEST_SESSION, isGuestSession);
        outState.putBoolean(STATE_IS_LIST_VISIBLE, listviewContainer.getVisibility() == View.VISIBLE);
    }
    // ----------------------------------------------------


    private void attemptLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        String user = "shadow";
        String pass = "1234567890";

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Credenciales de Prueba
        if (username.equals(user) && password.equals(pass)) {
            isGuestSession = false; // Login exitoso (NO invitado)
            Toast.makeText(this, "Bienvenido " + user, Toast.LENGTH_LONG).show();
            setupListView(); // Refrescar vista con contenido completo
            switchToListView();
        } else {
            Toast.makeText(this, "Usuario o contraseña inválidos.", Toast.LENGTH_LONG).show();
        }
    }

    private void switchToListView() {
        loginContainer.setVisibility(View.GONE);
        listviewContainer.setVisibility(View.VISIBLE);
    }

    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        if (listviewContainer.getVisibility() == View.VISIBLE) {
            listviewContainer.setVisibility(View.GONE);
            loginContainer.setVisibility(View.VISIBLE);
            // Limpia el estado de invitado al volver al login
            isGuestSession = false;
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setupListView() {
        // Enlazar componentes de main_app.xml (gracias a <include>)
        // Nota: Si hay errores de NPE aquí, se recomienda usar listviewContainer.findViewById(...)
        ListView lista = findViewById(R.id.my_list_view);
        texto = findViewById(R.id.footer_text);
        Button btnClearSelection = findViewById(R.id.btn_clear_selection);
        backToLoginButton = findViewById(R.id.btn_back_to_login);

        // 1. Configurar la acción del Botón de Volver
        if (backToLoginButton != null) {
            backToLoginButton.setOnClickListener(v -> {
                listviewContainer.setVisibility(View.GONE);
                loginContainer.setVisibility(View.VISIBLE);
                isGuestSession = false;
                Toast.makeText(this, "Volviendo al Login", Toast.LENGTH_SHORT).show();
            });
        }

        // 2. Crear la fuente de datos
        ArrayList<Encapsulador> datos = new ArrayList<>();
        // Asume que R.drawable.drsX existen en tu proyecto
        datos.add(new Encapsulador(R.drawable.drs1, "Dr.Stone Manga 1", "Dr.Stone Manga 1 disponible en amazon. La historia sigue a Senku Ishigami, un joven genio de la ciencia.", false));
        datos.add(new Encapsulador(R.drawable.drs2, "Dr.Stone Manga 2", "Dr.Stone Manga 2 disponible en amazon. Senku y Taiju comienzan la misión de despetrificar a la humanidad.", false));
        datos.add(new Encapsulador(R.drawable.drs3, "Dr.Stone Manga 3", "Dr.Stone Manga 3 disponible en amazon. La fundación del Reino de la Ciencia.", false));
        datos.add(new Encapsulador(R.drawable.drs4, "Dr.Stone Manga 4", "Dr.Stone Manga 4 disponible en amazon. Preparativos para la primera guerra con Tsukasa.", false));
        datos.add(new Encapsulador(R.drawable.drs5, "Dr.Stone Manga 5", "Dr.Stone Manga 5 disponible en amazon. Introducción de nuevos inventos científicos.", false));

        Adaptador adaptador = new Adaptador(this, R.layout.entrada, datos) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    Encapsulador datosEntrada = (Encapsulador) entrada;

                    // Enlazar vistas (IDs de tu entrada.xml)
                    ImageView imagen = view.findViewById(R.id.imagen_entrada);
                    TextView titulo = view.findViewById(R.id.texto_titulo);
                    TextView contenido = view.findViewById(R.id.texto_datos);
                    final RadioButton radioButton = view.findViewById(R.id.radio_button_entrada);

                    // Sincronizar datos y estado del RadioButton
                    imagen.setImageResource(datosEntrada.get_imagen());
                    titulo.setText(datosEntrada.get_textoTitulo());

                    // --- RESTRICCIÓN DE CONTENIDO VISUAL EN LISTA ---
                    if (isGuestSession) {
                        contenido.setText("Regístrate para ver la descripción.");
                        contenido.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else {
                        contenido.setText(datosEntrada.get_textoContenido());
                        contenido.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    }
                    // --------------------------------------------------

                    radioButton.setChecked(datosEntrada.get_CheckBox());
                    radioButton.setTag(datosEntrada);

                    radioButton.setOnClickListener(v -> {
                        RadioButton currentRadioButton = (RadioButton) v;
                        Encapsulador currentData = (Encapsulador) currentRadioButton.getTag();

                        // --- NUEVA LÓGICA DE NAVEGACIÓN (solo si no es invitado) ---
                        if (!isGuestSession) {
                            // 1. Inicia la nueva Activity
                            Intent intent = new Intent(MainActivity.this, DetalleActivity.class);
                            intent.putExtra(DetalleActivity.EXTRA_IMAGEN, currentData.get_imagen());
                            intent.putExtra(DetalleActivity.EXTRA_TITULO, currentData.get_textoTitulo());
                            intent.putExtra(DetalleActivity.EXTRA_CONTENIDO, currentData.get_textoContenido());
                            startActivity(intent);

                            // 2. Continúa con la lógica de la selección del RadioButton
                            if (radioButton_pulsado != null && radioButton_pulsado != currentRadioButton) {
                                radioButton_pulsado.setChecked(false);
                                Encapsulador oldData = (Encapsulador) radioButton_pulsado.getTag();
                                if (oldData != null) {
                                    oldData.set_CheckBox(false);
                                }
                            }
                            currentRadioButton.setChecked(true);
                            radioButton_pulsado = currentRadioButton;
                            currentData.set_CheckBox(true);

                            if (texto != null) {
                                texto.setText("Manga Selecinado: " + currentData.get_textoTitulo());
                            }
                        } else {
                            // 3. Si es invitado, muestra un aviso y no navega
                            Toast.makeText(MainActivity.this, "Debes iniciar sesión para ver los detalles del producto.", Toast.LENGTH_LONG).show();
                            // También puedes desmarcar forzosamente el radio button para evitar selección "falsa"
                            currentRadioButton.setChecked(false);
                        }
                        // -------------------------------------------------------------
                    });

                    if (datosEntrada.get_CheckBox()) {
                        radioButton_pulsado = radioButton;
                    }
                }
            }
        };
        lista.setAdapter(adaptador);

        // 3. IMPLEMENTACIÓN DEL BOTÓN DE DESMARCAR SELECCIÓN
        if (btnClearSelection != null) {
            btnClearSelection.setOnClickListener(v -> {
                if (radioButton_pulsado != null) {
                    radioButton_pulsado.setChecked(false);
                }
                for(Encapsulador item : datos) {
                    item.set_CheckBox(false);
                }
                radioButton_pulsado = null;
                adaptador.notifyDataSetChanged();

                if (texto != null) {
                    texto.setText("Elige un manga");
                }
            });
        }
    }

    public static class Encapsulador {
        private final int imagen;
        private final String titulo;
        private final String texto;
        private boolean dato;

        public Encapsulador(int imagen, String textTitulo, String textContenido, boolean selected) {
            this.imagen = imagen;
            this.titulo = textTitulo;
            this.texto = textContenido;
            this.dato = selected;
        }

        public int get_imagen() { return imagen; }
        public String get_textoTitulo() { return titulo; }
        public String get_textoContenido() { return texto; }
        public boolean get_CheckBox() { return dato; }
        public void set_CheckBox(boolean estado) { this.dato = estado; }
    }
}