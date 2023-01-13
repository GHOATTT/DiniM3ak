/*
 * Copyright 2015 Andre.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.dinim3akalpha001.service.directions;

import com.example.dinim3akalpha001.javascript.JavascriptObject;
import com.example.dinim3akalpha001.javascript.object.GMapObjectType;
import java.util.logging.Level;
import java.util.logging.Logger;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;

/**
 *
 * @author Andre
 */
public class Distance extends JavascriptObject {
    
    public Distance(JSObject jsObject){
        super(GMapObjectType.DISTANCE, jsObject);
    }
        
    public Distance(double value, String text){
        super(GMapObjectType.DISTANCE, value, text);   
    }
    
    public Double getValue(){
        try{
            return (1.0 * (Integer) getJSObject().getMember("value"));
        } catch(JSException | NumberFormatException e){
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "", e);
        }
        return null;
    }
    
    public String getText(){
        try{
            return (String) getJSObject().getMember("text");
        } catch(JSException | NumberFormatException e){
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "", e);
        }
        return null;
    }
    
}
