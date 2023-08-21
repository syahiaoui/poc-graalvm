package com.zenika.poc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class GraalVMExample {
    public static void main(String[] args) {
        try (Context context = Context.create()) {
            String JS_CODE = """
                    function greet(person) {
                        const json =JSON.parse(JSON.stringify(person));
                        console.log(json);
                        console.log(person.hobbies.length);
                        return 'Hello, ' + person.name + '!'; }
                    """;
            // Create a Java object instance
            String[] hobbies = {"Reading", "Painting"};

            Person person = new Person("John", 30, hobbies);
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(person);

            // Expose the Java object to the JavaScript context
            context.getBindings("js").putMember("person", jsonInString);
            // Execute a JavaScript function that uses the Java object
            context.eval("js", JS_CODE);

            // Invoke the JavaScript function with the Java object as an argument
            Value result = context.eval("js", "greet("+ jsonInString +")");

            // Get the result and print it
            System.out.println(result.asString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        try (Context context = Context.create()) {
//            // Execute the script containing the modified greet function
//            final String JS_CODE = """
//                    function greet(person) {
//                    console.log(JSON.stringify(person))
//                      if (person.firstName && person.lastName && person.title) {
//                        return "Hello, " + person.title + " " + person.firstName + " " + person.lastName + "!";
//                      } else {
//                        throw new Error("Invalid person object. It should have firstName, lastName, and title properties.");
//                      }
//                    }
//                    """;
//
//            context.eval("js", JS_CODE);
//
//            // Create a dynamic Java object representing the person
//            String firstName = "John";
//            String lastName = "Doe";
//            String title = "Mr.";
//            Value person = context.asValue(new Person(firstName, lastName, title));
//
//            // Access the greet function and invoke it with the person object as an argument
//            Value function = context.getBindings("js").getMember("greet");
//            Value result = function.execute(person);
//
//            // Print the result
//            System.out.println(result.asString());
//        }
    }
}
