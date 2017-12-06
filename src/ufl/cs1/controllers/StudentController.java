package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.Defender;
import game.models.Game;

import java.util.List;

public final class StudentController implements DefenderController
{
	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int[] update(Game game,long timeDue)
	{
		int[] actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();
		Defender dominic = enemies.get(1);
		Defender mohona = enemies.get(3);

		actions[1] = chaseMethod(game, dominic);
		actions[3] = mohonaAction(mohona, game, timeDue);


		return actions;
	}

	public int mohonaAction(Defender mohona, Game game, long timeDue) {
		int mohonasMove = -1;
		int i = 0;
		int min = 0;
		int minNode = 0;
		int distance = 0;
		int j = 0;

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
			//Move towards that pill
			mohonasMove = mohona.getNextDir(game.getPowerPillList().get(minNode), true);
		}
		//no pills, chase attacker.
		else {
			mohonasMove = mohona.getNextDir(game.getAttacker().getLocation(), true);
		}

		return mohonasMove;
	}

	public int chaseMethod(Game game, Defender ghost)
	{
		int action;
		//Assigns action int to direction the ghost should go in to follow Ms. Pacman
		action = ghost.getNextDir(game.getAttacker().getLocation(),true);

		return action;
	}
}
