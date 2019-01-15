/*
 * Copyright (C) 2018 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.syndesis.simulator;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Christoph Deppisch
 */
@ContextConfiguration(classes = SimulatorConfig.class)
public class TodoClientIT extends JUnit4CitrusTestDesigner {

    /** Test Http REST client */
    @Autowired
    private HttpClient todoClient;

    @Test
    @CitrusTest
    public void testAddTodo() {
        variable("id", "citrus:randomNumber(10)");
        variable("task", "Walk the dog");
        variable("completed", 0);

        http().client(todoClient)
                .send()
                .post("/")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .payload(new ClassPathResource("templates/todo.json"));

        http().client(todoClient)
                .receive()
                .response(HttpStatus.OK);
    }

    @Test
    @CitrusTest
    public void testDeleteTodo() {
        variable("id", "citrus:randomNumber(10)");

        http().client(todoClient)
                .send()
                .delete("/${id}");

        http().client(todoClient)
                .receive()
                .response(HttpStatus.OK);
    }

    @Test
    @CitrusTest
    public void testGetTodoById() {
        variable("id", "citrus:randomNumber(10)");

        http().client(todoClient)
                .send()
                .get("/${id}")
                .accept(MediaType.APPLICATION_JSON_VALUE);

        http().client(todoClient)
                .receive()
                .response(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .payload(new ClassPathResource("templates/todo-control.json"));
    }

    @Test
    @CitrusTest
    public void testUpdateTodo() {
        variable("id", "citrus:randomNumber(10)");
        variable("task", "Feed the dog");
        variable("completed", 1);

        http().client(todoClient)
                .send()
                .put("/${id}")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .payload(new ClassPathResource("templates/todo.json"));

        http().client(todoClient)
                .receive()
                .response(HttpStatus.OK);
    }

    @Test
    @CitrusTest
    public void testListTodo() {
        http().client(todoClient)
                .send()
                .get("/")
                .accept(MediaType.APPLICATION_JSON_VALUE);

        http().client(todoClient)
                .receive()
                .response(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .payload("[ citrus:readFile(templates/todo-control.json) ]");
    }

    @Test
    @CitrusTest
    public void testUnsupportedOperation() {
        http().client(todoClient)
                .send()
                .post("/something/completely/different");

        http().client(todoClient)
                .receive()
                .response(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .messageType(MessageType.PLAINTEXT)
                .payload("Unsupported operation on path '/todo/api/something/completely/different'");
    }
}
