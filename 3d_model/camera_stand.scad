$fn = 50;

use<servo.scad>;

//#translate([0, 0, 0]) servo();
//#translate([8, -18, 22 + 29.2]) rotate([90, 0, 180]) servo();

outer_fn = 18;
inner_fn = 180;

!translate([-14, 12, 55]) cam_mount();

difference() {
    translate([0, 0, 0]) {
        base();
        top();
    }
    translate([0, 60, 24]) {
        //cube([100, 50, 50], true);
    }
}

module base() {
    difference() {
        cylinder(d = 100, h = 3, $fn = outer_fn);
        translate([0, 0, -0.1]) scale([1.075, 1, 1]) servo();
       
        // Cut-out's
        translate([-23, 17, -1]) { 
            minkowski() {
                cube([46, 16, 10]);
                cylinder(d = 6, h = 1);
            }
        }
        translate([-23, -33, -1]) { 
            minkowski() {
                cube([46, 16, 10]);
                cylinder(d = 6, h = 1);
            }
        }
    }
    
    // Servo mount
    translate([17, -6, 0]) difference() {
        translate([0, -1, 0]) {
            translate([0, 0, 0]) cube([5, 14, 16]);
            translate([4.1, 0, 0]) 
                rotate([0, -15, 0]) cube([5, 14, 15.2]);
        }
        translate([2, 6, 5]) cylinder(d = 2, h = 14);
    }
    translate([-11.25, -6, 0]) difference() {
        translate([0, -1, 0]) {
            cube([5, 14, 16]);
            translate([-3.95, 0, 1.3]) 
                rotate([0, 15, 0]) cube([5, 14, 15.2]);
        }
        translate([-4, 2, 0]) cube([10, 8, 10]);
        translate([3, 6, 5]) cylinder(d = 2, h = 14);
    }
    
    // Ring
    difference() {
        translate([0, 0, 0]) {
            cylinder(d = 100, h = 3, $fn = outer_fn);
            translate([0, 0, 23]) cylinder(d = 100, h = 8, $fn = outer_fn);
            
            parts = 9;
            diameter = 72.55;
            for(i = [0:parts]) {
               rotate(i * 360 / parts + 8.8)
                    translate([30, diameter / 2, 0]) {
                        rotate([90, 0, 151.2]) cube([17.4, 24, 3]);
                    }
            }
        }
        translate([0, 0, -1]) cylinder(d = 94, h = 34, $fn = inner_fn);
        translate([0, 0, 26.8]) cylinder(d = 97, h = 5.1, $fn = inner_fn);
        
    }
}

module top() {
    y_offset = 2;
    z_offset = 0;
    translate([0, 0, 27 + z_offset]) {
        difference() {
            cylinder(d = 96.4, h = 4, $fn = inner_fn);
            translate([0, 0, -7]) cylinder(d = 4.5, h = 10);
            translate([0, 0, -4]) cylinder(d = 2.5, h = 10);
            
            // Pi Mount
            translate([0, 22, 9]) 
                rotate([0, 0, 180]) {
                    //#import("raspberry_pi_zero_2_w.stl");
                }
                
            // Cut-out Pi
            translate([-23, 7 + y_offset, -5]) { 
                minkowski() {
                    cube([46, 22, 10]);
                    cylinder(d = 6, h = 1);
                }
            }
            
            // Cut-out cables
            translate([-23, -28, -5]) { 
                minkowski() {
                    cube([46, 6, 10]);
                    cylinder(d = 6, h = 1);
                }
            }
            
            // Pi mount holes
            translate([0, y_offset, 0]) pi_mounting_holes();
        }
    
        // Pi mount
        difference() {
            translate([0, y_offset, 4]) {
                translate([-29, 29.5, 0]) cylinder(d = 6, h = 6);
                translate([-29, 6.5, 0]) cylinder(d = 6, h = 6);
                translate([29, 29.5, 0]) cylinder(d = 6, h = 6);
                translate([29, 6.5, 0]) cylinder(d = 6, h = 6);
            }
            translate([0, y_offset, 3.1]) pi_mounting_holes();
        }
        
        // Servo mount
        difference() {
            translate([8, 8, 0]) {
                translate([6.2, -20, 0]) cube([5, 10, 30]);
                translate([-21.8, -20, 0]) cube([5, 10, 30]);
                translate([-21.8, -20, 15]) cube([30, 10, 3]);
                
                translate([6.2, -25, 3]) 
                    rotate([-15, 0, 0]) cube([5, 10, 20]);
                translate([-21.8, -25, 3]) 
                    rotate([-15, 0, 0]) cube([5, 10, 20]);
                
                translate([6.2, -16, 0]) 
                    rotate([15, 0, 0]) cube([5, 10, 16.5]);
                translate([-21.8, -16, 0]) 
                    rotate([15, 0, 0]) cube([5, 10, 16.5]);
            }
            translate([16.1, 0, 24.2]) 
                rotate([90, 0, 0]) 
                    cylinder(d = 2, h = 16);
            translate([-10.9, 0, 24.2]) 
                rotate([90, 0, 0]) 
                    cylinder(d = 2, h = 16);
        }
    }

}

module cam_mount() {
    translate([13.7, 0, -3.5]) {
        #translate([4, 2, 4.6]) cube([12, 3, 0.4]);
        #translate([4, 11.5, 4.6]) cube([12, 3, 0.4]);
    }
    
    difference() {
        translate([13.7, 0, -3.5]) {
            cube([16, 16, 5]);

        }
        translate([14, 20, 1]) rotate([90, 0, 0]) cylinder(d = 4.5, h = 25);
        translate([18, 4, -2.5]) cube([12, 8.6, 5]);
    }
    translate([14, 2, 1]) rotate([90, 0, 0]) difference() {
        cylinder(d = 9, h = 5);
        translate([0, 0, 1]) cylinder(d = 4.5, h = 8);
        translate([0, 0, -3]) cylinder(d = 2.5, h = 8);
    }
}

module pi_mounting_holes() {
    translate([0, 0, -1]) {
        translate([-29, 29.5, 0]) cylinder(d = 2.7, h = 8);
        translate([-29, 6.5, 0]) cylinder(d = 2.7, h = 8);
        translate([29, 29.5, 0]) cylinder(d = 2.7, h = 8);
        translate([29, 6.5, 0]) cylinder(d = 2.7, h = 8);
    }
}
