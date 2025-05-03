module ru.nstu.labbi {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.nstu.labbi to javafx.fxml;
    opens ru.nstu.labbi.Controllers to javafx.fxml;
    exports ru.nstu.labbi;
}