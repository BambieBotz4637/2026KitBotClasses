// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OIConstants;
import frc.robot.auto.AutoDrive;
//import frc.robot.auto.ExampleAuto;
import frc.robot.commands.Eject;
import frc.robot.commands.Intake;
import frc.robot.commands.LaunchSequence;
import frc.robot.subsystems.CANFuelSubsystem;
import frc.robot.subsystems.DriveSubsystem;

/*
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.events.EventTrigger;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;
*/

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  private final DriveSubsystem driveSubsystem = new DriveSubsystem();
  private final CANFuelSubsystem fuelSubsystem = new CANFuelSubsystem();

  // The driver's controller
   GenericHID m_driverController = new  GenericHID(OIConstants.kDriverControllerPort);
   
  // The operator's controller
  private final CommandXboxController m_operatorController = new CommandXboxController(1);
   

  // The autonomous chooser
  private final SendableChooser<Command> m_chooser = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureBindings();
      // Add commands to the autonomous command chooser
    m_chooser.setDefaultOption("Auto Drive", new AutoDrive(driveSubsystem));
    m_chooser.addOption("Launch Auto", new LaunchSequence(fuelSubsystem));
    Shuffleboard.getTab("Autonomous").add(m_chooser).withWidget(BuiltInWidgets.kComboBoxChooser);

    NamedCommands.registerCommand("Intake",new Intake(fuelSubsystem));
    NamedCommands.registerCommand("Launch",new LaunchSequence(fuelSubsystem));
    NamedCommands.registerCommand("Eject",new Eject(fuelSubsystem));
  }

  

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the {@link Trigger#Trigger(java.util.function.BooleanSupplier)}
   * constructor with an arbitrary predicate, or via the named factories in
   * {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses
   * for {@link CommandXboxController Xbox}/
   * {@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {

    new JoystickButton(m_driverController, 6)
        .whileTrue(new RunCommand(
            () -> driveSubsystem.setX(),
            driveSubsystem));

    new JoystickButton(m_driverController, 8)
        .onTrue(new InstantCommand(
            () -> driveSubsystem.zeroHeading(),
            driveSubsystem));

  
    // While the left bumper on operator controller is held, intake Fuel
    m_operatorController.leftBumper().whileTrue(new Intake(fuelSubsystem));
   /*
    m_operatorController.leftBumper().whileFalse(new InstantCommand(
      () -> fuelSubsystem.stop(),
      fuelSubsystem));
      */

    // While the right bumper on the operator controller is held, spin up for 1
    // second, then launch fuel. When the button is released, stop.
    m_operatorController.rightBumper().whileTrue(new LaunchSequence(fuelSubsystem));
    /*
    m_operatorController.rightBumper().whileFalse(new InstantCommand(
      () -> fuelSubsystem.stop(),
      fuelSubsystem));
      */

    // While the A button is held on the operator controller, eject fuel back out
    // the intake
    m_operatorController.y().whileTrue(new Eject(fuelSubsystem));
   /* 
    m_operatorController.y().whileFalse(new InstantCommand(
      () -> fuelSubsystem.stop(),
      fuelSubsystem)); 
      */


  // Set the default command for the drive subsystem to the command provided by
  // factory with the values provided by the joystick axes on the driver
  // controller. The Y axis of the controller is inverted so that pushing the
  // stick away from you (a negative value) drives the robot forwards (a positive
  // value)

driveSubsystem.setDefaultCommand(
    new RunCommand(
        () -> driveSubsystem.drive(
            -m_driverController.getRawAxis(1),
            -m_driverController.getRawAxis(0),
            -m_driverController.getRawAxis(4),
            true),
            driveSubsystem));
  
   // fuelSubsystem.setDefaultCommand(fuelSubsystem.run(() -> fuelSubsystem.stop()));          
  }
   /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   * */
   
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
   // System.out.println("Selected Auto: " + m_chooser.getSelected());
   return m_chooser.getSelected();
      }
    }

