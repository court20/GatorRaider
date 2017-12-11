package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.Defender;
import game.models.Game;
import game.models.Node;

import java.util.List;
import game.models.Attacker;

public final class StudentController implements DefenderController
{
	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int[] update(Game game,long timeDue)
	{
		int[] actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();
		Defender courtney = enemies.get(0);
		Defender dominic = enemies.get(1);
		Defender jonathan= enemies.get(2);
		Defender mohona = enemies.get(3);

		actions[0] = courtneysMethod(courtney, game);
		actions[1] = chaseMethod(game, dominic);
		actions[2] = jonathansMethod(jonathan, game);
		actions[3] = mohonaAction(mohona, game);

		if(courtney.getLocation().getX()== dominic.getLocation().getX() ||courtney.getLocation().getX()== jonathan.getLocation().getX() ||courtney.getLocation().getX()== mohona.getLocation().getX()||courtney.getLocation().getY()== dominic.getLocation().getY() ||courtney.getLocation().getY()== jonathan.getLocation().getY() ||courtney.getLocation().getY()== mohona.getLocation().getY()){
			if(courtney.getLocation().getX()== dominic.getLocation().getX() || courtney.getLocation().getY()== dominic.getLocation().getY()){
				actions[0]=dominic.getReverse();
			}else if(courtney.getLocation().getX()== jonathan.getLocation().getX() || courtney.getLocation().getY()== jonathan.getLocation().getY()){
				actions[0]=jonathan.getReverse();
			}else if(courtney.getLocation().getX()== mohona.getLocation().getX() || courtney.getLocation().getY()== mohona.getLocation().getY()){
				actions[0]=mohona.getReverse();
			}
		}

		int i = 0;
		for (Defender name : game.getDefenders()){
			if (name.isVulnerable()) {
				actions[i] = name.getNextDir(game.getAttacker().getLocation(), false);
				i++;
			}
		}

		return actions;
	}

	public int almostVulnerable(Defender name, Game game, int x) {
		int action = -1;
		int i = 0;
		int min = 0;
		int minNode = 0;
		int distance = 0;
		int j = 0;

		//find the pill pacman is closest to
		if (game.getPowerPillList().size() != 0) {
			for (i = 1; i < game.getPowerPillList().size() && i > 0; i++) {
				if (game.checkPowerPill(game.getPowerPillList().get(i)) && game.checkPowerPill(game.getPowerPillList().get(j))) {
					if (i == 1) {
						min = game.getAttacker().getLocation().getPathDistance(game.getPowerPillList().get(j));
						minNode = j;
					}
					distance = game.getAttacker().getLocation().getPathDistance(game.getPowerPillList().get(i));
					if (distance < min) {
						min = distance;
						minNode = i;
					}
				} else {
					j++;
				}
			}

			//if the path to the attacker is short enough, run away
			if (game.getPowerPillList().get(minNode).getPathDistance(game.getAttacker().getLocation()) <= x) {
				for (int k = 0; k < name.getPathTo(game.getAttacker().getLocation()).size(); k++) {
					if (name.getPathTo(game.getAttacker().getLocation()).get(k).getX() <= x || name.getPathTo(game.getAttacker().getLocation()).get(k).getX() <= x) {
						action = name.getReverse();
						return action;
					}
				}
			}

		}

		return action;

	}

	public int mohonaAction(Defender mohona, Game game) {
		int mohonasMove = -1;
		int i = 0;
		int min = 0;
		int minNode = 0;
		int distance = 0;
		int j = 0;
		//Finding the nearest pill
		if (game.getPowerPillList().size() != 0) {
			//Get lists of pills
			for (i = 1; i < game.getPowerPillList().size() && i > 0 ; i++) {
				//ensure that there is a power pill at the node
				if (game.checkPowerPill(game.getPowerPillList().get(i)) && game.checkPowerPill(game.getPowerPillList().get(j))) {
					//Find the pill pacman is nearest to.
					if (i == 1) {
						min = game.getAttacker().getLocation().getPathDistance(game.getPowerPillList().get(j));
						minNode = j;
					}
					distance = game.getAttacker().getLocation().getPathDistance(game.getPowerPillList().get(i));
					if (distance < min) {
						min = distance;
						minNode = i;
					}
				}
				else{
					j++;
				}
			}

			//if i am close to pacman and pacman is close to a pill, go in the reverse direction
			if (almostVulnerable(mohona, game, 20) != -1 ) {
				mohonasMove = almostVulnerable(mohona, game, 20);
			}
			else {
				//otherwise, move towards that pill
				mohonasMove = mohona.getNextDir(game.getPowerPillList().get(minNode), true);
			}
		}
		//no pills, chase attacker.
		else {
			mohonasMove = mohona.getNextDir(game.getAttacker().getLocation(), true);
		}

		return mohonasMove;
	}

	public int jonathansMethod(Defender jon, Game game){
		int direction;
		Attacker pacman= game.getAttacker();
		Node location= pacman.getLocation();
		Node target;

		if (almostVulnerable(jon, game, 20 ) != -1 ) {
			direction = almostVulnerable(jon, game, 20);
		}
		else {

			target=pacman.getLocation();
			try{
				target=  location.getNeighbor(pacman.getDirection());
				try{
					target=  target.getNeighbor(pacman.getDirection());
					try{
						target=  target.getNeighbor(pacman.getDirection());
						try{
							target=  target.getNeighbor(pacman.getDirection());

						}catch(NullPointerException h){}

					}catch(NullPointerException e){}

				}catch(NullPointerException j){}

			}catch(NullPointerException k){

			}
			try{
				direction = jon.getNextDir(target, true);
			}catch(NullPointerException l){
				direction=jon.getNextDir(location,true);
			}

		}
		return direction;
	}

	public int chaseMethod(Game game, Defender ghost)
	{
		int action;
		if (almostVulnerable(ghost, game, 5) != -1 ) {
			action = almostVulnerable(ghost, game, 5);
		}
		else {
			//Assigns action int to direction the ghost should go in to follow Ms. Pacman
			action = ghost.getNextDir(game.getAttacker().getLocation(), true);
		}
		return action;
	}

	//courtney's method for getting the attacker (normal mode)
	public int courtneysMethod(Defender courtney, Game game){
		int direction;
		if (almostVulnerable(courtney, game, 20) != -1 ) {
			direction = almostVulnerable(courtney, game, 20);
		}
		else {
			//gets the node facing the way the attacker is going
			direction = courtney.getNextDir(game.getAttacker().getLocation().getNeighbor(game.getAttacker().getReverse()), true);
		}
		return direction;
	}
}