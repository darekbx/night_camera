
module servo() {
    difference() {
        union() {
            translate([-5.9,-11.8/2,0]) cube([22.5,11.8,22.7]);
            translate([0,0,22.7-0.1]){
                cylinder(d=11.8,h=4+0.1);
                hull(){
                    translate([8.8-5/2,0,0]) cylinder(d=5,h=4+0.1);
                    cylinder(d=5,h=4+0.1);
                }
                translate([0,0,4]) cylinder(d=4.6,h=3.2);
            }
            translate([-4.7-5.9,-11.8/2,15.9]) cube([22.5+4.7*2, 11.8, 2.5]); 
        }
        //screw holes
        translate([-2.3-5.9,0,15.9+1.25]) cylinder(d=2,h=5, center=true);
        translate([-2.3-5.9-2,0,15.9+1.25]) cube([3,1.3,5], center=true);
        translate([2.3+22.5-5.9,0,15.9+1.25]) cylinder(d=2,h=5, center=true);
        translate([2.3+22.5-5.9+2,0,15.9+1.25]) cube([3,1.3,5], center=true);
    }
}