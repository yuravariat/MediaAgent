package haifa.university.mediaagent.common;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yura on 27/02/2016.
 */
public class GenericResponse<T> {
    public T data;
    public ArrayList<Warning> warnings;
    public ArrayList<Error> errors;

    public boolean isOK(){
        return errors==null || errors.size()==0;
    }
    public boolean hasWarnings(){
        return warnings!=null || warnings.size()>0;
    }
    public boolean hasErrors(){
        return errors!=null || errors.size()>0;
    }
    public boolean hasData(){
        return data!=null;
    }
    public void addWarning(String massage){
        addWarning(0, massage);
    }
    public void addWarning(int code, String message){
        if(warnings==null){
            warnings = new ArrayList<>();
        }
        warnings.add(new Warning(code, message));
    }
    public void addWarnings(List<Warning> warns){
        if(warnings==null){
            warnings = new ArrayList<>();
        }
        warnings.addAll(warns);
    }
    public void addError(String massage){
        addError(0, massage);
    }
    public void addError(Exception exception){
        addError(0, exception.getMessage(), exception);
    }
    public void addError(int code, String message){
        addError(0, message, null);
    }
    public void addError(int code, String message, Exception exception){
        if(errors==null){
            errors = new ArrayList<>();
        }
        errors.add(new Error(code, message));
    }
    public void addErrors(List<Error> errs){
        if(errors==null){
            errors = new ArrayList<>();
        }
        errors.addAll(errs);
    }
    public String getErrorsJSON(){
        if(errors!=null){
            Gson gson = new Gson();
            return gson.toJson(errors);
        }
        return "";
    }
    public class Warning{
        public int code;
        public String message;
        public Warning(int code,String message){
            this.code = code;
            this.message = message;
        }
    }
    public class Error extends Warning{
        public Exception exception;
        public Error(int code,String message){
            super(code,message);
            this.exception = exception;
        }
        public Error(int code,String message,Exception exception){
            super(code,message);
            this.exception = exception;
        }
    }
}
