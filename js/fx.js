(function (global) {
  // Avoid multiple inclusion
  if (global.fx != undefined) {
    return;
  }
  
  // Imports
  // -------
  var System = java.lang.System;
  var Application = Packages.javafx.application.Application;
  var ApplicationAdapter = Packages.com.github.crossfx.ApplicationAdapter;
  var File = java.io.File;

  global.fx = {};

  // FX Launch Method
  // ----------------
  
  fx.launch = function(onStart){
    ApplicationAdapter.onStart = function (stage) {
      importJavaFxTypes();
      onStart(stage);
    };
    var ApplicationAdapterClass = java.lang.Class.forName('com.github.crossfx.ApplicationAdapter');
    Application.launch(ApplicationAdapterClass, []);
  }
  
  // FX Controller
  // -------------

  var controllerMixin = {};
  
  fx.Controller = function(callback){
    var self = this;
    Object.keys(controllerMixin).forEach(function(key){
      self[key] = controllerMixin[key];
    });
    
    if(callback){
      callback(this);
    }
  };
  
  controllerMixin.fxml = function (path) {
    var SystemClass = java.lang.Class.forName('java.lang.System');
    
    // Create the ControllerAdapter to send to the CrossFXMLLoader
    var controllerAdapter = buildControllerAdapter(this);
    
    var fxmlUrl;
    
    // Try to read from file system directly, then from class path
    var file = new File(path);
    if(file.exists() && !file.isDirectory()) {
      fxmlUrl = file.toURI().toURL();
    } else {
      // Try to get the url from the classpath, making the path
      // absolute if relative lookup fails
      fxmlUrl = SystemClass.getResource(path);
      if(!fxmlUrl) fxmlUrl = SystemClass.getResource("/" + path);
    }
    
    if(!fxmlUrl) {
      throw new java.lang.Exception("Could not find fxml file: " + path);
    }
    
    // Load the fxml
    var fxmlLoader = this.fxmlLoader() || new fx.CrossFxmlLoader(fxmlUrl);
    fxmlLoader.controller = controllerAdapter;
    this.fxmlLoader(fxmlLoader);
    this.scene = new fx.Scene(fxmlLoader.load());
    return this;
  };
  
  controllerMixin.fxmlLoader = function (fxmlLoader) {
    if(fxmlLoader){
      this._fxmlLoader = fxmlLoader;
      return this;
    } else {
      return this._fxmlLoader;
    }
  }
  
  // Must be called after .fxml(...) method,
  // as it expects this.scene to be set already
  controllerMixin.stage = function (stage) {
    if (arguments.length == 0) return this._stage;
    stage.scene = this.scene;
    this._stage = stage;
    return this;
  };
  
  controllerMixin.show = function () {
    if(!this.stage()){
      throw 'Cannot show controller with no stage. Use `stage(...)` method to set stage';
    }
    this.stage().show();
  };
  
  controllerMixin.hide = function () {
    this.stage().hide();
  };

  // Called by JavaFX to initialize a controller after its root element
  // has been fully processed (say, after the fxml has been loaded)
  controllerMixin.initialize = function () {
    var self = this;
    
    // Extend controller with all fields in fxml namespace
    var namespace = self.fxmlLoader().namespace;
    var keySet = namespace.keySet().toArray();
    for(var index in keySet){
      // Ignore the controller field in the namespace. This is the controller
      if(keySet[index] == "controller") continue;
      // Don't overwrite fields defined by the client code
      if(self[keySet[index]] !== undefined) {
        System.out.println(
          'Warning: Attempt to set field "' + 
          keySet[index] + 
          '" on controller from FXMLLoader namespace failed. ' +
          'A field by this name is already defined');
        continue;
      }
      self[keySet[index]] = namespace.get(keySet[index]);
    }
  }

  // Converts a JavaScript controller object into the java Class "ControllerAdapter"
  // The ControllerAdapter holds a hash of the available methods and nested controllers.
  function buildControllerAdapter(controller){
    var controllerAdapter = new Packages.com.github.crossfx.ControllerAdapter();
    
    controllerAdapter.onSetFxmlLoader(function (fxmlLoader) {
      controller.fxmlLoader(fxmlLoader);
    });
    
    Object.keys(controller).forEach(function(field){
      if(typeof controller[field] == 'function'){
        // Add method for current controller
        controllerAdapter.addMethod(field ,function(argArray){
          controller[field](argArray);
        });
      } else if (isNestedControllerField(field)) {
        controllerAdapter.addNestedController(
          field.slice(0, -("Controller".length)),
          buildControllerAdapter(controller[field])
        );
      }
    });
    return controllerAdapter;
  }
  
  function isNestedControllerField(fieldName){
    var lastIndexOfController = fieldName.lastIndexOf("Controller");
    return ( lastIndexOfController != -1 &&
             lastIndexOfController == fieldName.length - "Controller".length)
  }
  
  // Event Hub
  // ---------
  
  fx.EventHub = function () {
    var listeners = {};
    
    this.on = function (event, callback){
      if (listeners[event] == undefined){
        listeners[event] = [];
      }
      
      var callbackAlreadyRegistered = false;
      for(var i = 0; i < listeners[event].length; i++){
        if (listeners[event][i] === callback) {
          callbackAlreadyRegistered = true;
          break;
        }
      }
      
      if (callbackAlreadyRegistered) return;
      listeners[event].push(callback);
    }
    
    this.off = function (event, callback){
      for(var i = 0; i < listeners[event].length; i++){
        if (listeners[event][i] === callback) {
          listeners[event].splice(i, 1);
          return;
        }
      }
    }
    
    this.emit = function (event, args, context) {
      if(!listeners[event]) return;
      
      if(typeof args !== 'object' || args.constructor.name !== 'Array'){
        args = [args];
      }
      
      listeners[event].forEach(function (listener) {
        listener.apply(context, args);
      });
    }
  }
  
  // Properties Utilities
  // --------------------

  // TODO: Move these to an extension
  
  fx.observable = function (initialValue) {
    // TODO: Handle exceptional initialValue values (null, undefined)
    if (typeof initialValue == 'object' && initialValue.constructor.name == 'Array') {
      return new fx.SimpleListProperty(
        fx.FXCollections.observableArrayList(initialValue)
      );
    }
    
    if (typeof initialValue === 'undefined') {
      Packages.com.github.crossfx.ObservableAdapter(null);
    }
    
    return new Packages.com.github.crossfx.ObservableAdapter(initialValue);
  };
  
  fx.computed = function (computer) {
    if (typeof computer == 'function') {
      return new Computed(computer);
    } else {
      return new WriteableComputed(computer);
    }
  };
  
  fx.bind = function (observable) {
    var registrar = Packages.com.github.crossfx.DependencyRegistrar.instance();
    registrar.registerDependency(observable);
    return observable.value;
  }
  
  function Computed(computer) {
    Computed.Class = Computed.Class || Packages.com.github.crossfx.ComputedAdapter;
    return new Computed.Class(computer);
  }
  
  function WriteableComputed(computer) {
    WriteableComputed.Class = WriteableComputed.Class || Packages.com.github.crossfx.WriteableComputedAdapter;    return new WriteableComputed.Class(computer.read, computer.write);
  }
  
  // Chooser Helpers
  // ---------------
  
  fx.openFile = function (stageTitle) {
    var stage = new fx.Stage();
    var chooser = new fx.FileChooser();
    if (stageTitle) chooser.title = stageTitle;
    return chooser.showOpenDialog(stage);
  };
  
  fx.saveFile = function (stageTitle) {
    var stage = new fx.Stage();
    var chooser = new fx.FileChooser();
    if (stageTitle) chooser.title = stageTitle;
    return chooser.showSaveDialog(stage);
  };
  
  fx.chooseDirectory = function (stageTitle) {
    var stage = new fx.Stage();
    var chooser = new fx.DirectoryChooser();
    if (stageTitle) chooser.title = stageTitle;
    return chooser.showDialog(stage);
  };
  
  // Listener Helpers
  // ----------------
  
  fx.addChangeListener = function (property, callback) {
    property.addListener(new fx.ChangeListener({
      changed: callback
    }));
  }
  
  // Background Task Helper
  // ----------------------
  
  fx.runInBackground = function (callback){
    (new java.lang.Thread(callback)).start();
  };
  
  // importJavaFxTypes Function
  // --------------------------
  
  // Note: This function MUST be called within fx.launch,
  // as many of these classes cannot be accessed outside of a JavaFX
  // application thread
  function importJavaFxTypes () {
    
    // com.github.crossfx
    fx.CrossFxmlLoader = Packages.com.github.crossfx.CrossFxmlLoader;
    
    // javafx.application
    fx.Platform = Packages.javafx.application.Platform;
    
    // javafx.beans.property
    fx.SimpleBooleanProperty = Packages.javafx.beans.property.SimpleBooleanProperty;
    fx.SimpleDoubleProperty = Packages.javafx.beans.property.SimpleDoubleProperty;
    fx.SimpleListProperty = Packages.javafx.beans.property.SimpleListProperty;
    fx.SimpleObjectProperty = Packages.javafx.beans.property.SimpleObjectProperty;
    fx.SimpleStringProperty = Packages.javafx.beans.property.SimpleStringProperty;
    
    // javafx.beans.value
    fx.ChangeListener = Packages.javafx.beans.value.ChangeListener;
    
    // javafx.collections
    fx.FXCollections = Packages.javafx.collections.FXCollections;
    fx.ObservableList = Packages.javafx.collections.ObservableList;
    
    // javafx.fxml
    fx.FxmlLoader = Packages.javafx.fxml.FXMLLoader;
    fx.JavaFXBuilderFactory = Packages.javafx.fxml.JavaFXBuilderFactory;

    // javafx.geometry
    fx.Insets = Packages.javafx.geometry.Insets;
    
    // javafx.scene
    fx.Scene = Packages.javafx.scene.Scene;
    fx.Stage = Packages.javafx.stage.Stage;
    
    // javafx.scene.control
    fx.Button = Packages.javafx.scene.control.Button;
    fx.Button = Packages.javafx.scene.control.Button;
    fx.CheckBox = Packages.javafx.scene.control.CheckBox;
    fx.ContentDisplay = Packages.javafx.scene.control.ContentDisplay;
    fx.Label = Packages.javafx.scene.control.Label;
    fx.ListCell = Packages.javafx.scene.control.ListCell;
    fx.TableCell = Packages.javafx.scene.control.TableCell;
    fx.TableColumn = Packages.javafx.scene.control.TableColumn;
    fx.TextField = Packages.javafx.scene.control.TextField;
    fx.TableView = Packages.javafx.scene.control.TableView;
    fx.Tooltip = Packages.javafx.scene.control.Tooltip;
    
    // javafx.scene.layout
    fx.StackPane = Packages.javafx.scene.layout.StackPane;
    fx.GridPane = Packages.javafx.scene.layout.GridPane;
    fx.HBox = Packages.javafx.scene.layout.HBox;
    fx.VBox = Packages.javafx.scene.layout.VBox;
    
    // javafx.stage
    fx.DirectoryChooser = Packages.javafx.stage.DirectoryChooser;
    fx.FileChooser = Packages.javafx.stage.FileChooser;
  };
}(this));