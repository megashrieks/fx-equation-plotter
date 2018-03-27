/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equationplotter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 *
 * @author shrikanth
 */
public class EquationPlotter extends Application {
    Scene scene;
    int divisions = 30;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ep.fxml"));
        scene = new Scene(root);
        Button redraw = (Button) scene.lookup("#redraw");
        Canvas can = (Canvas) scene.lookup("#eqn_plot");
        GraphicsContext gc = can.getGraphicsContext2D();
        drawAxis(gc,can);
        redraw.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                drawIt(gc,can);
            }
        });
        primaryStage.setTitle("Equation Plotter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void drawIt(GraphicsContext gc,Canvas c){
        String x_eqn = ((TextField) scene.lookup("#x_eqn")).getText();
        String y_eqn = ((TextField) scene.lookup("#y_eqn")).getText();
        double t_start = Double.parseDouble(((TextField) scene.lookup("#t_start")).getText());
        double t_end = Double.parseDouble(((TextField) scene.lookup("#t_end")).getText());
        double t_step = Double.parseDouble(((TextField) scene.lookup("#t_step")).getText());
        drawAxis(gc,c);
        
        //ExpressionTree etX = new ExpressionTree(x_eqn);
        //ExpressionTree etY = new ExpressionTree(y_eqn);
        double width_offset = c.getWidth()/2;
        double height_offset = c.getHeight()/2;
        double characters[] = new double[26];
        gc.setFill(Color.BLACK);
        for(double i = t_start;i <= t_end;i+=t_step){
            characters['t'-'a'] = i;
            //double x = etX.calculate(characters)*divisions+width_offset;
            //double y = etY.calculate(characters)*divisions+height_offset;
            
            //when the ExpressionTree class is available
            //the following lines will be replaced by the above lines
            double x = (0.6*Math.cos(i)*(1-Math.cos(i))*divisions)+width_offset;
            double y = (0.6*Math.sin(i)*(1-Math.cos(i))-Math.sin(2*i)*divisions)+height_offset;
            //drawing each point
            gc.fillArc(x, y, 2, 2, 45, 240, ArcType.ROUND);
        }
        
    }
    
    public void drawAxis(GraphicsContext gc,Canvas c){
        //dimensions of canvas
        double width = c.getWidth();
        double height = c.getHeight();
        //set Default Color and stroke length for axis
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        
        //empty the canvas
        gc.fillRect(0,0,width,height);
        
        
        //divisions
        long x_divisions = Math.round(width / divisions)/2;
        long y_divisions = Math.round(height / divisions)/2;
        
        //x-axis
        gc.strokeLine(0, height/2, width, height/2);
        //y-axis
        gc.strokeLine(width/2, 0, width/2, height);
        
        //numbers
        gc.setLineWidth(0.51);
        gc.setStroke(Color.BLACK);
        //y-axis numbers
        //height/2-height
        int temp = 1;
        for(double i = height/2+divisions;i <= height;i+=divisions){
            gc.strokeLine(width/2-10, i, width/2+10, i);
            gc.strokeText(""+(temp++), width/2+13, i+4);
        }
        //height/2-0
        temp = -1;
        for(double i = height/2-divisions;i >= 0;i-=divisions){
            gc.strokeLine(width/2-10, i, width/2+10, i);
            gc.strokeText(""+(temp--), width/2+13, i+4);
        }
        
        
        //x-axis numbers
        //width/2-width
        temp = 1;
        for(double i = width/2+divisions;i <= width;i+=divisions){
            gc.strokeLine(i, height/2-10, i, height/2+10);
            gc.strokeText(""+(temp++), i-4, height/2+22);
        }
        //height/2-0
        temp = -1;
        for(double i = width/2-divisions;i >= 0;i-=divisions){
            gc.strokeLine(i, height/2-10, i, height/2+10);
            gc.strokeText(""+(temp--), i-4, height/2+22);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
