/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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

package opta.boot.vehiclerouting.controller;

import opta.boot.vehiclerouting.domain.JsonCustomer;
import opta.boot.vehiclerouting.domain.JsonMessage;
import opta.boot.vehiclerouting.domain.JsonVehicleRoute;
import opta.boot.vehiclerouting.domain.JsonVehicleRoutingSolution;
import opta.boot.vehiclerouting.service.VehicleRoutingSolverManager;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.examples.vehiclerouting.domain.Customer;
import org.optaplanner.examples.vehiclerouting.domain.Vehicle;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.domain.location.Location;
import org.optaplanner.swing.impl.TangoColorFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VehicleRoutingController {

    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#,##0.00");


    private VehicleRoutingSolverManager solverManager;

    public VehicleRoutingController(VehicleRoutingSolverManager solverManager) {
        this.solverManager = solverManager;
    }

    @GetMapping("/vehiclerouting")
    public String vehiclerouting() {
        return "vehiclerouting/index";
    }

    @GetMapping("/vehiclerouting/leaflet")
    public String leaflet() {
        return "vehiclerouting/leaflet";
    }

    @GetMapping("/vehiclerouting/gmaps")
    public String gmaps() {
        return "vehiclerouting/googleMaps";
    }

    @ResponseBody
    @GetMapping(value = "/vehiclerouting/solution", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonVehicleRoutingSolution getSolution(HttpServletRequest request) {
        VehicleRoutingSolution solution = solverManager.retrieveOrCreateSolution(request.getSession().getId());
        return convertToJsonVehicleRoutingSolution(solution);
    }

    private JsonVehicleRoutingSolution convertToJsonVehicleRoutingSolution(VehicleRoutingSolution solution) {
        JsonVehicleRoutingSolution jsonSolution = new JsonVehicleRoutingSolution();
        jsonSolution.setName(solution.getName());
        List<JsonCustomer> jsonCustomerList = new ArrayList<>(solution.getCustomerList().size());
        for (Customer customer : solution.getCustomerList()) {
            Location customerLocation = customer.getLocation();
            jsonCustomerList.add(new JsonCustomer(customerLocation.getName(),
                    customerLocation.getLatitude(), customerLocation.getLongitude(), customer.getDemand()));
        }
        jsonSolution.setCustomerList(jsonCustomerList);
        List<JsonVehicleRoute> jsonVehicleRouteList = new ArrayList<>(solution.getVehicleList().size());
        TangoColorFactory tangoColorFactory = new TangoColorFactory();
        for (Vehicle vehicle : solution.getVehicleList()) {
            JsonVehicleRoute jsonVehicleRoute = new JsonVehicleRoute();
            Location depotLocation = vehicle.getDepot().getLocation();
            jsonVehicleRoute.setDepotLocationName(depotLocation.getName());
            jsonVehicleRoute.setDepotLatitude(depotLocation.getLatitude());
            jsonVehicleRoute.setDepotLongitude(depotLocation.getLongitude());
            jsonVehicleRoute.setCapacity(vehicle.getCapacity());
            Color color = tangoColorFactory.pickColor(vehicle);
            jsonVehicleRoute.setHexColor(
                    String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
            Customer customer = vehicle.getNextCustomer();
            int demandTotal = 0;
            List<JsonCustomer> jsonVehicleCustomerList = new ArrayList<>();
            while (customer != null) {
                Location customerLocation = customer.getLocation();
                demandTotal += customer.getDemand();
                jsonVehicleCustomerList.add(new JsonCustomer(customerLocation.getName(),
                        customerLocation.getLatitude(), customerLocation.getLongitude(), customer.getDemand()));
                customer = customer.getNextCustomer();
            }
            jsonVehicleRoute.setDemandTotal(demandTotal);
            jsonVehicleRoute.setCustomerList(jsonVehicleCustomerList);
            jsonVehicleRouteList.add(jsonVehicleRoute);
        }
        jsonSolution.setVehicleRouteList(jsonVehicleRouteList);
        HardSoftLongScore score = solution.getScore();
        jsonSolution.setFeasible(score != null && score.isFeasible());
        jsonSolution.setDistance(solution.getDistanceString(NUMBER_FORMAT));
        return jsonSolution;
    }

    @ResponseBody
    @PostMapping(value = "/vehiclerouting/solution/solve", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage solve(HttpServletRequest request) {
        boolean success = solverManager.solve(request.getSession().getId());
        return new JsonMessage(success ? "Solving started." : "Solver was already running.");
    }

    @ResponseBody
    @PostMapping(value = "/vehiclerouting/solution/terminateEarly", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonMessage terminateEarly(HttpServletRequest request) {
        boolean success = solverManager.terminateEarly(request.getSession().getId());
        return new JsonMessage(success ? "Solver terminating early." : "Solver was already terminated.");
    }

}
