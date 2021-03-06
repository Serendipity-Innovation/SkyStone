/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.attachments.ShadowCaster;
import org.firstinspires.ftc.teamcode.drivetrain.DrivetrainWheel;
import org.firstinspires.ftc.teamcode.odometry.OdometryCalculationsParallel;
import org.firstinspires.ftc.teamcode.odometry.OdometryGraphing;

import java.io.IOException;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Drive r Station OpMode list
 */

@Autonomous(name="AutonomousDemo", group="Iterative Opmode")
//@Disabled
public class AutonomousDemo extends OpMode
{
    private DcMotor autonomousLeftWheel = null;
    private DcMotor autonomousRightWheel = null;
    private DrivetrainWheel autonomousWheels = null;
    private ShadowCaster shadowCaster = null;
    private DcMotor linearActuator = null;
    private Servo uvCover = null;
    private OdometryGraphing odometryGraph = null;

    private ElapsedTime runtime = new ElapsedTime();
    @Override
    public void init() {
        // make drivetrain
        DrivetrainWheel MotorBoatDrivetrain = new DrivetrainWheel(hardwareMap);
        autonomousWheels = MotorBoatDrivetrain;
        autonomousLeftWheel = MotorBoatDrivetrain.getLeftWheel();
        autonomousRightWheel = MotorBoatDrivetrain.getRightWheel();

        // make odometry
        OdometryGraphing odometryGraph = new OdometryGraphing(hardwareMap);

        // make shadowcaster
        ShadowCaster shadowCaster = new ShadowCaster(hardwareMap);
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        autonomousWheels.moveWheelsFeetPower(2);
        shadowCaster.moveLinearActuatorUp(1120);
        shadowCaster.uncoverLight();
        // DO CV DETECTION
        boolean detectedGerms = true; // change this with the real cv detection boolean
        try {
            odometryGraph.graphData(detectedGerms);
        } catch (IOException e) {
            e.printStackTrace();
        }
        shadowCaster.coverLight();
        shadowCaster.moveLinearActuatorDown(1120);

        autonomousWheels.turnAngle(90);
        telemetry.addData("Status", "Run Time: " + runtime.toString());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {telemetry.addData("Ending Note", "Good Job Team!");
    }

}
