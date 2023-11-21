package com.rra.meetingRoomMgt.Controller;

import com.rra.meetingRoomMgt.Service.UnitsService;

import com.rra.meetingRoomMgt.modal.Units;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/rra/v1/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitsService unitsService;

    @GetMapping(path = "/listall")
    public ResponseEntity<List<Units>> getAllUnits() {
        List<Units> units = unitsService.getAllUnits();
        return new ResponseEntity<>(units, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUnit(@RequestBody Units units) {
        Object result = unitsService.saveUnits(units);
        if (result != null) {
            return ResponseEntity.ok(Map.of("msg", "unit created successfuly", "unit", result));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUnits(@RequestBody Units units) {
        Object result = unitsService.updateUnits(units);
        if (result != null) {
            return ResponseEntity.ok(Map.of("msg", "unit Updated successfuly", "unitUpdated", result));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/delete")
    public ResponseEntity<Object> deleteUnits(@RequestBody Units units) {
        int id = units.getUnitID();
        Object deleteUnit = unitsService.deleteUnits(id);

        if (deleteUnit != null) {
            return ResponseEntity.ok(Map.of("msg", "unit deleted successful", "id", id, "deletedUnt", deleteUnit));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("msg", "unit not found for ID: " + id));
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Object> findUnitById(@PathVariable Integer id) {
        Object units = unitsService.findUnitsById(id);
        if (units != null) {
            return ResponseEntity.ok(units);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
